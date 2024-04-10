package com.example.riskanalysissoftwareprototype.controllers;

import com.example.riskanalysissoftwareprototype.MonteCarloItems.EvenSimulation;
import com.example.riskanalysissoftwareprototype.MonteCarloItems.MonteCarloSimulation;
import com.example.riskanalysissoftwareprototype.MonteCarloItems.NormalSimulation;
import com.example.riskanalysissoftwareprototype.MonteCarloItems.PoissonSimulation;
import org.json.JSONException;
import org.json.JSONObject;

public class MonteCarloController {

    private static MonteCarloController instance = null;
    private MonteCarloSimulation design;
    private MonteCarloSimulation development;
    private MonteCarloSimulation testing;

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
            this.development = parseDistributions(jsonData.getJSONObject("testing"));
            this.testing = parseDistributions(jsonData.getJSONObject("development"));
            return "Saved";
        } catch (JSONException e) {
            return "Failure to Save";
        }
    }

    //SUPPORT METHODS

    private MonteCarloSimulation parseDistributions(JSONObject data) {

        switch (data.getString("distribution")) {
            case "Normal":
                break;
            case "Poisson":
                break;
            case "Even":
                break;
            default:
                throw new JSONException("Data Failure");
        }
    }

    private MonteCarloSimulation parseDesign(JSONObject design) {
        String designOptimistic = design.getString("optimistic");
        String designLikely = design.getString("likely");
        String designPessimistic = design.getString("pessimistic");
        String designMean = design.getString("mean");
        String designStdDeviation = design.getString("stdDeviation");
    }

    private MonteCarloSimulation parseDevelopment(JSONObject development) {
        String developmentOptimistic = development.getString("optimistic");
        String developmentLikely = development.getString("likely");
        String developmentPessimistic = development.getString("pessimistic");
        String developmentMean = development.getString("mean");
        String developmentStdDeviation = development.getString("stdDeviation");
    }

    private MonteCarloSimulation parseTesting(JSONObject testing) {
        String testingOptimistic = testing.getString("optimistic");
        String testingLikely = testing.getString("likely");
        String testingPessimistic = testing.getString("pessimistic");
        String testingMean = testing.getString("mean");
        String testingStdDeviation = testing.getString("stdDeviation");
    }

}
