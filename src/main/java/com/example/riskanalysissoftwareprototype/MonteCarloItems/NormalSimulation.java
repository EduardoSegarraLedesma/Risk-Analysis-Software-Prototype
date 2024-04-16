package com.example.riskanalysissoftwareprototype.MonteCarloItems;

public class NormalSimulation extends MonteCarloSimulation implements IMonteCarloSimulation {
    public NormalSimulation(int mOptimistic, int mLikely, int mPessimistic, int mDuration, int sDeviation) {
        super(mOptimistic, mLikely, mPessimistic, mDuration, sDeviation);
    }

    @Override
    public int doDistribution() {
        return 0;
    }
}
