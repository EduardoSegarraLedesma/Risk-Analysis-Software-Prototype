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

    private Connection connection;

    private PersistenceController() {
    }

    public static PersistenceController getInstance() {
        if (instance == null)
            instance = new PersistenceController();
        return instance;
    }

    public void saveProject(WorkSchedule project) throws SQLException {
        Connection connection = RiskAnalysisDB().getConnection();
        int projectID = getProjectID(connection, project.getProjectName());
        if (projectID != -1) {
            updateProject(connection, project, projectID);
        } else {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Projects VALUES (?)");
            statement.setString(1, project.getProjectName());
            statement.execute();
            saveSchedule(connection, project);
            connection.close();
        }
    }

    public LinkedList<String> retrieveAllProjectNames() throws SQLException {
        LinkedList<String> list = new LinkedList<>();
        connection = RiskAnalysisDB().getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT ProjectName FROM Projects");
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            list.add(result.getString(1));
        }
        connection.close();
        return list;
    }

    public WorkSchedule retrieveProjectSchedule(String projectName) throws SQLException {
        connection = RiskAnalysisDB().getConnection();
        int projectID = getProjectID(connection, projectName);
        PreparedStatement statement = connection.prepareStatement(
                "SELECT Phase, OptimisticDuration, LikelyDuration, PessimisticDuration, MeanDuration, " +
                        "StandardDeviation, DistributionType FROM Schedules WHERE projectID = ?");
        statement.setInt(1, projectID);
        ResultSet result = statement.executeQuery();
        WorkSchedule retrieved = new WorkSchedule(convertToJSON(projectName, result));
        connection.close();
        return retrieved;
    }

    private void updateProject(Connection connection, WorkSchedule project, int projectID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Projects SET ProjectName = ? WHERE ProjectID = ? ;");
        statement.setString(1, project.getProjectName());
        statement.setInt(2, projectID);
        statement.executeUpdate();
        updateSchedule(connection, project, projectID);
    }

    public void deleteProject(String projectName) throws SQLException {
        Connection connection = RiskAnalysisDB().getConnection();
        int projectID = getProjectID(connection, projectName);
        deleteSchedule(connection, projectID);
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Projects WHERE ProjectID = ?;");
        statement.setInt(1, projectID);
        statement.execute();
        connection.close();
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

    private int getProjectID(Connection connection, String projectName) throws SQLException {
        int projectID;
        PreparedStatement statement = connection.prepareStatement(
                "SELECT ProjectId FROM Projects WHERE ProjectName = ? ");
        statement.setString(1, projectName);
        ResultSet result = statement.executeQuery();
        if (result.next())
            projectID = result.getInt(1);
        else projectID = -1;
        return projectID;
    }

    private void saveSchedule(Connection connection, WorkSchedule schedule) throws SQLException {
        int projectID = getProjectID(connection, schedule.getProjectName());
        savePhase(connection, projectID, schedule.getDesign(), "Design");
        savePhase(connection, projectID, schedule.getDevelopment(), "Development");
        savePhase(connection, projectID, schedule.getTesting(), "Testing");
    }

    private void savePhase(Connection connection, int projectID, IMonteCarloSimulation phase, String phaseName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Schedules VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setInt(1, projectID);
        statement.setString(2, phaseName);
        statement.setInt(3, phase.getMostOptimistic());
        statement.setInt(4, phase.getMostLikely());
        statement.setInt(5, phase.getMostPessimistic());
        statement.setInt(6, phase.getMeanDuration());
        statement.setInt(7, phase.getStandardDeviation());
        statement.setString(8, phase.getDistribution());
        statement.execute();

    }

    private String convertToJSON(String projectName, ResultSet result) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("name", projectName);
        ObjectNode designNode = rootNode.putObject("design");
        ObjectNode developmentNode = rootNode.putObject("development");
        ObjectNode testingNode = rootNode.putObject("testing");
        while (result.next()) {
            String phase = result.getString("Phase");
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

    private void updateSchedule(Connection connection, WorkSchedule schedule, int projectID) throws SQLException {
        updatePhase(connection, projectID, schedule.getDesign(), "Design");
        updatePhase(connection, projectID, schedule.getDevelopment(), "Development");
        updatePhase(connection, projectID, schedule.getTesting(), "Testing");
    }

    private void updatePhase(Connection connection, int projectID, IMonteCarloSimulation phase, String phaseName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Schedules SET OptimisticDuration = ?, LikelyDuration = ?, PessimisticDuration = ?, " +
                        "MeanDuration  = ?, StandardDeviation  = ?, DistributionType = ? " +
                        "WHERE ProjectID = ? AND Phase = ?");
        statement.setInt(1, phase.getMostOptimistic());
        statement.setInt(2, phase.getMostLikely());
        statement.setInt(3, phase.getMostPessimistic());
        statement.setInt(4, phase.getMeanDuration());
        statement.setInt(5, phase.getStandardDeviation());
        statement.setString(6, phase.getDistribution());
        statement.setInt(7, projectID);
        statement.setString(8, phaseName);
        statement.executeUpdate();
    }

    private void deleteSchedule(Connection connection, int projectID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Schedules WHERE ProjectID = ?");
        statement.setInt(1, projectID);
        statement.execute();
    }
}

