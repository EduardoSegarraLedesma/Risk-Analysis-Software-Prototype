package com.example.riskanalysissoftwareprototype.MonteCarloItems;

import org.apache.commons.math3.distribution.TriangularDistribution;

public class TriangularSimulation extends MonteCarloSimulation implements IMonteCarloSimulation {
    public TriangularSimulation(int mOptimistic, int mLikely, int mPessimistic, int mDuration, int sDeviation) {
        super(mOptimistic, mLikely, mPessimistic, mDuration, sDeviation);
    }

    @Override
    public int doDistribution() {
        TriangularDistribution distribution = new TriangularDistribution(mOptimistic, mLikely, mPessimistic);
        return (int) Math.round(distribution.sample());
    }
}
