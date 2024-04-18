package com.example.riskanalysissoftwareprototype.MonteCarloItems;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;

public class EvenSimulation extends MonteCarloSimulation implements IMonteCarloSimulation {
    public EvenSimulation(int mOptimistic, int mLikely, int mPessimistic, int mDuration, int sDeviation) {
        super(mOptimistic, mLikely, mPessimistic, mDuration, sDeviation);
    }

    @Override
    public int doDistribution() {
        UniformIntegerDistribution distribution = new UniformIntegerDistribution(mOptimistic, mPessimistic);
        return Math.round(distribution.sample());
    }
}
