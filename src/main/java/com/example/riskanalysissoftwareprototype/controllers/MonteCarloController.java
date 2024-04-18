package com.example.riskanalysissoftwareprototype.controllers;

import com.example.riskanalysissoftwareprototype.MonteCarloItems.*;
import org.json.JSONException;
import org.json.JSONObject;

public class MonteCarloController {

    private static MonteCarloController instance = null;
    private IMonteCarloSimulation design;
    private IMonteCarloSimulation development;
    private IMonteCarloSimulation testing;

    private int[][] simulation;
    private int maxDuration;
    private int minDuration;

    private MonteCarloController() {

    }

    public static MonteCarloController getInstance() {
        if (instance == null)
            instance = new MonteCarloController();
        return instance;
    }

    public String ParseData(String data) {
        try {
            JSONObject jsonData = new JSONObject(data);
            this.design = parseDistributions(jsonData.getJSONObject("design"));
            this.development = parseDistributions(jsonData.getJSONObject("development"));
            this.testing = parseDistributions(jsonData.getJSONObject("testing"));
            return "Saved";
        } catch (JSONException e) {
            return "Error in input data";
        }
    }

    public int[][] doSimulation() {
        minDuration = Integer.MAX_VALUE;
        maxDuration = Integer.MIN_VALUE;
        cleanSimulationArray();
        for (int i = 0; i < simulation.length; i++) {
            int total = 0;
            simulation[i][0] = design.doDistribution();
            total += simulation[i][0];
            simulation[i][1] = development.doDistribution();
            total += simulation[i][1];
            simulation[i][2] = testing.doDistribution();
            total += simulation[i][2];
            simulation[i][3] = total;
            if (total > maxDuration)
                maxDuration = total;
            else if (total < minDuration)
                minDuration = total;
        }
        return simulation;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public int getMinDuration() {
        return minDuration;
    }

    //SUPPORT METHODS

    private void cleanSimulationArray() {
        simulation = new int[400][4];
    }

    private IMonteCarloSimulation parseDistributions(JSONObject data) {
        int opt = Integer.parseInt(data.getString("optimistic"));
        int lik = Integer.parseInt(data.getString("likely"));
        int pes = Integer.parseInt(data.getString("pessimistic"));
        int mea = Integer.parseInt(data.getString("mean"));
        int dev = Integer.parseInt(data.getString("stdDeviation"));
        return switch (data.getString("distribution")) {
            case "Normal" -> new NormalSimulation(opt, lik, pes, mea, dev);
            case "Triangular" -> new TriangularSimulation(opt, lik, pes, mea, dev);
            case "Even" -> new EvenSimulation(opt, lik, pes, mea, dev);
            default -> throw new JSONException("Data Failure");
        };
    }

}