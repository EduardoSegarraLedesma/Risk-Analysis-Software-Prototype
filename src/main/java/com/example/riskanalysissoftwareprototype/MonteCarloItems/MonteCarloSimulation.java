package com.example.riskanalysissoftwareprototype.MonteCarloItems;

public class MonteCarloSimulation {

    protected int mOptimistic;
    protected int mLikely;
    protected int mPessimistic;
    protected int mDuration;
    protected int sDeviation;
    protected String distribution;

    public MonteCarloSimulation(int mOptimistic, int mLikely, int mPessimistic, int mDuration, int sDeviation, String distribution) {
        this.mOptimistic = mOptimistic;
        this.mLikely = mLikely;
        this.mPessimistic = mPessimistic;
        this.mDuration = mDuration;
        this.sDeviation = sDeviation;
        this.distribution = distribution;

    }

    public int getMostOptimistic() {
        return mOptimistic;
    }

    public int getMostLikely() {
        return mLikely;
    }

    public int getMostPessimistic() {
        return mPessimistic;
    }

    public int getMeanDuration() {
        return mDuration;
    }

    public int getStandardDeviation() {
        return sDeviation;
    }

    public String getDistribution() {
        return distribution;
    }
}
