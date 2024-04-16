package com.example.riskanalysissoftwareprototype.MonteCarloItems;

public class MonteCarloSimulation {

    private int mOptimistic;
    private int mLikely;

    private int mPessimistic;
    private int mDuration;
    private int sDeviation;

    public MonteCarloSimulation(int mOptimistic, int mLikely, int mPessimistic, int mDuration, int sDeviation) {
        this.mOptimistic = mOptimistic;
        this.mLikely = mLikely;
        this.mPessimistic = mPessimistic;
        this.mDuration = mDuration;
        this.sDeviation = sDeviation;
    }

    public int doDistribution() {
        return 0;
    }
}
