package com.example.riskanalysissoftwareprototype.controllers;

import com.example.riskanalysissoftwareprototype.MonteCarloItems.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MonteCarloController {

    private static MonteCarloController instance = null;
    private IMonteCarloSimulation design;
    private IMonteCarloSimulation development;
    private IMonteCarloSimulation testing;
    private static final int simulationIterations = 400;
    private int[][] simulation;
    private int maxDuration;
    private int minDuration;
    private float[][] percentages;

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
        simulation = new int[simulationIterations][4];
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

    public float[][] obtainResultsPercentages() {
        initializePercentagesArray();
        countResultRepetitions();
        calculateResultPercentages();
        return percentages;
    }

    public int getDaysAccordingToRisk(int risk) {
        for (int i = 0; i < percentages.length; i++)
            if (percentages[i][2] >= (1f - ((float) risk / 100f)))
                return (int) percentages[i][0];
        return -1;
    }

    //SUPPORT METHODS
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

    private void initializePercentagesArray() {
        percentages = new float[maxDuration - minDuration + 1][3];
        for (int i = 0; i < percentages.length; i++) {
            percentages[i][0] = minDuration + i;
            percentages[i][1] = 0;
        }
    }

    private void countResultRepetitions() {
        initializePercentagesArray();
        for (int i = 0; i < simulation.length; i++) {
            int simulationResult = simulation[i][3];
            percentages[simulationResult - minDuration][1] += 1;
        }
    }

    private void calculateResultPercentages() {
        float cumulativeProbability = 0;
        for (int i = 0; i < percentages.length; i++) {
            percentages[i][1] = percentages[i][1] / simulationIterations;
            cumulativeProbability += percentages[i][1];
            percentages[i][2] = round(cumulativeProbability, 3);
        }
    }

    private float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, RoundingMode.HALF_UP).floatValue();
    }

}