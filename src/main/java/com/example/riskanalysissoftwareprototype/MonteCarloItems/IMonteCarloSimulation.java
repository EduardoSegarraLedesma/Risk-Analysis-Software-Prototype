package com.example.riskanalysissoftwareprototype.MonteCarloItems;

public interface IMonteCarloSimulation {
    int getMostOptimistic();

    int getMostLikely();

    int getMostPessimistic();

    int getMeanDuration();

    int getStandardDeviation();

    String getDistribution();

    int doDistribution();
}
