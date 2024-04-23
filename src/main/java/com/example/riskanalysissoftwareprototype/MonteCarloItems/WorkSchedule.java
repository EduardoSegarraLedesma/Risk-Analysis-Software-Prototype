package com.example.riskanalysissoftwareprototype.MonteCarloItems;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkSchedule {

    private final String projectName;
    private final IMonteCarloSimulation design;
    private final IMonteCarloSimulation development;
    private final IMonteCarloSimulation testing;
    private final String JSONdata;

    public WorkSchedule(String data) throws JSONException {
        this.JSONdata = data;
        JSONObject jsonData = new JSONObject(data);
        this.projectName = jsonData.getString("name");
        this.design = parseJSONdistributions(jsonData.getJSONObject("design"));
        this.development = parseJSONdistributions(jsonData.getJSONObject("development"));
        this.testing = parseJSONdistributions(jsonData.getJSONObject("testing"));
    }

    public String getProjectName() {
        return projectName;
    }

    public IMonteCarloSimulation getDesign() {
        return design;
    }

    public IMonteCarloSimulation getDevelopment() {
        return development;
    }

    public IMonteCarloSimulation getTesting() {
        return testing;
    }

    public String getJSONschedule() {
        return JSONdata;
    }

    //SUPPORT METHODS
    private IMonteCarloSimulation parseJSONdistributions(JSONObject data) {
        return switch (data.getString("distribution")) {
            case "Normal" -> new NormalSimulation(parseValue(data, "optimistic"),
                    parseValue(data, "likely"),
                    parseValue(data, "pessimistic"),
                    parseValue(data, "mean"),
                    parseValue(data, "stdDeviation"));
            case "Triangular" -> new TriangularSimulation(parseValue(data, "optimistic"),
                    parseValue(data, "likely"),
                    parseValue(data, "pessimistic"),
                    parseValue(data, "mean"),
                    parseValue(data, "stdDeviation"));
            case "Even" -> new EvenSimulation(parseValue(data, "optimistic"),
                    parseValue(data, "likely"),
                    parseValue(data, "pessimistic"),
                    parseValue(data, "mean"),
                    parseValue(data, "stdDeviation"));
            default -> throw new JSONException("Data Failure");
        };
    }

    private int parseValue(JSONObject data, String value) {
            return Integer.parseInt(data.getString(value));
    }
}
