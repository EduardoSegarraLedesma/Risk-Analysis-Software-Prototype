package com.example.riskanalysissoftwareprototype.controllers;

import com.example.riskanalysissoftwareprototype.MonteCarloItems.IMonteCarloSimulation;
import com.example.riskanalysissoftwareprototype.MonteCarloItems.WorkSchedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;

public class PersistenceController {
    private static PersistenceController instance = null;

    private PersistenceController() {
    }

    public static PersistenceController getInstance() {
        if (instance == null)
            instance = new PersistenceController();
        return instance;
    }

    public void saveProject(WorkSchedule project) throws SQLException {
        int projectID = getProjectID(project.getProjectName());
        if (projectID != -1) {
            updateProject(project, projectID);
        } else {
            Connection connection = RiskAnalysisDB().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO Projects "
                    + "VALUES ('" + project.getProjectName() + "');");
            saveSchedule(project, statement);
            close(statement, connection);
        }
    }

    public LinkedList<String> retrieveAllProjectNames() throws SQLException {
        LinkedList<String> list = new LinkedList<>();
        Connection connection = RiskAnalysisDB().getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(
                "SELECT ProjectName FROM Projects;");
        while (result.next()) {
            list.add(result.getString(1));
        }
        return list;
    }

    public WorkSchedule retrieveProjectSchedule(String projectName) throws SQLException {
        int projectID = getProjectID(projectName);
        Connection connection = RiskAnalysisDB().getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT " +
                "Phase, OptimisticDuration, LikelyDuration, PessimisticDuration, MeanDuration, StandardDeviation, DistributionType " +
                "FROM Schedules WHERE projectID = " + projectID + ";");
        WorkSchedule retrieved = new WorkSchedule(convertToJSON(projectName, result));
        close(statement, connection);
        return retrieved;
    }

    private void updateProject(WorkSchedule project, int projectID) throws SQLException {
        Connection connection = RiskAnalysisDB().getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE Projects " +
                "SET ProjectName = '" + project.getProjectName() + "' " +
                "WHERE ProjectID = '" + projectID + "';");
        updateSchedule(project, projectID, statement);
        close(statement, connection);
    }

    public void deleteProject(String projectName) throws SQLException {
        int projectID = getProjectID(projectName);
        Connection connection = RiskAnalysisDB().getConnection();
        Statement statement = connection.createStatement();
        deleteSchedule(projectID, statement);
        statement.execute("DELETE FROM Projects WHERE ProjectID = " + projectID + ";");
        close(statement, connection);
    }


    //SUPPORT METHODS
    private DataSource RiskAnalysisDB() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://risk-analysis-demo.database.windows.net:1433;database=risk-analysis-db");
        dataSource.setUsername("CloudSA4a7a7e1b");
        dataSource.setPassword("Tongji_Root");
        return dataSource;
    }

    private void close(Statement statement, Connection connection) throws SQLException {
        statement.close();
        connection.close();
    }

    private int getProjectID(String projectName) throws SQLException {
        int projectID;
        Connection connection = RiskAnalysisDB().getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT ProjectId FROM Projects"
                + " WHERE ProjectName = '" + projectName + "';");
        if (result.next())
            projectID = result.getInt(1);
        else projectID = -1;
        close(statement, connection);
        return projectID;
    }

    private void saveSchedule(WorkSchedule schedule, Statement statement) throws SQLException {
        int projectID = getProjectID(schedule.getProjectName());
        savePhase(projectID, schedule.getDesign(), "Design", statement);
        savePhase(projectID, schedule.getDevelopment(), "Development", statement);
        savePhase(projectID, schedule.getTesting(), "Testing", statement);
    }

    private void savePhase(int projectID, IMonteCarloSimulation phase, String phaseName, Statement statement) throws SQLException {
        statement.execute("INSERT INTO Schedules "
                + "VALUES ("
                + "" + projectID + ","
                + "'" + phaseName + "',"
                + "" + phase.getMostOptimistic() + ","
                + "" + phase.getMostLikely() + ","
                + "" + phase.getMostPessimistic() + ","
                + "" + phase.getMeanDuration() + ","
                + "" + phase.getStandardDeviation() + ","
                + "'" + phase.getDistribution() + "');");
    }

    private String convertToJSON(String projectName, ResultSet result) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", projectName);
        ObjectNode designNode = rootNode.putObject("design");
        ObjectNode developmentNode = rootNode.putObject("development");
        ObjectNode testingNode = rootNode.putObject("testing");
        while (result.next()) {
            String phase = result.getString("Phase");  // Assuming there's a Phase column to differentiate
            String optimistic = String.valueOf(result.getInt("OptimisticDuration"));
            String likely = String.valueOf(result.getInt("LikelyDuration"));
            String pessimistic = String.valueOf(result.getInt("PessimisticDuration"));
            String mean = String.valueOf(result.getInt("MeanDuration"));
            String stdDeviation = String.valueOf(result.getInt("StandardDeviation"));
            String distribution = String.valueOf(result.getString("DistributionType"));
            ObjectNode phaseNode = switch (phase) {
                case "Design" -> designNode;
                case "Development" -> developmentNode;
                case "Testing" -> testingNode;
                default -> throw new IllegalStateException("Unexpected value: " + phase);
            };
            phaseNode.put("optimistic", optimistic);
            phaseNode.put("likely", likely);
            phaseNode.put("pessimistic", pessimistic);
            phaseNode.put("mean", mean);
            phaseNode.put("stdDeviation", stdDeviation);
            phaseNode.put("distribution", distribution);
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            throw new SQLException();
        }
    }

    private void updateSchedule(WorkSchedule schedule, int projectID, Statement statement) throws SQLException {
        updatePhase(projectID, schedule.getDesign(), "Design", statement);
        updatePhase(projectID, schedule.getDevelopment(), "Development", statement);
        updatePhase(projectID, schedule.getTesting(), "Testing", statement);
    }

    private void updatePhase(int projectID, IMonteCarloSimulation phase, String phaseName, Statement statement) throws SQLException {
        statement.executeUpdate("UPDATE Schedules " +
                "SET OptimisticDuration = " + phase.getMostOptimistic() + ", " +
                "LikelyDuration = " + phase.getMostOptimistic() + ", " +
                "PessimisticDuration = " + phase.getMostOptimistic() + ", " +
                "MeanDuration  = " + phase.getMostOptimistic() + ", " +
                "StandardDeviation  = " + phase.getMostOptimistic() + ", " +
                "Distribution = '" + phase.getDistribution() + "' " +
                "WHERE ProjectID = '" + projectID + "' " +
                "AND Phase = '" + phaseName + "';");
    }

    private void deleteSchedule(int projectID, Statement statement) throws SQLException {
        statement.execute("DELETE FROM Schedules WHERE ProjectID = " + projectID + ";");
    }
}

