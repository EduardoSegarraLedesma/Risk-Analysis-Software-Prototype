package com.example.riskanalysissoftwareprototype.controllers;

import com.example.riskanalysissoftwareprototype.MonteCarloItems.*;
import org.json.JSONException;
import org.json.JSONObject;

public class MonteCarloController {

    private static MonteCarloController instance = null;
    private IMonteCarloSimulation design;
    private IMonteCarloSimulation development;
    private IMonteCarloSimulation testing;

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

    private IMonteCarloSimulation parseDistributions(JSONObject data) {
        int opt = Integer.parseInt(data.getString("optimistic"));
        int lik = Integer.parseInt(data.getString("likely"));
        int pes = Integer.parseInt(data.getString("pessimistic"));
        int mea = Integer.parseInt(data.getString("mean"));
        int dev = Integer.parseInt(data.getString("stdDeviation"));
        return switch (data.getString("distribution")) {
            case "Normal" -> new NormalSimulation(opt, lik, pes, mea, dev);
            case "Poisson" -> new PoissonSimulation(opt, lik, pes, mea, dev);
            case "Even" -> new EvenSimulation(opt, lik, pes, mea, dev);
            default -> throw new JSONException("Data Failure");
        };
    }

}
