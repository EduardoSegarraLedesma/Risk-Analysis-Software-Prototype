package com.example.riskanalysissoftwareprototype.MonteCarloItems;

public class MonteCarloSimulation implements NormalSimulation, PoissonSimulation, EvenSimulation {

    private int mOptimistic;
    private int mLikely;
    private int mDuration;
    private int sDeviation;

    public MonteCarloSimulation(int mOptimistic, int mLikely, int mDuration, int sDeviation) {
        this.mOptimistic = mOptimistic;
        this.mLikely = mLikely;
        this.mDuration = mDuration;
        this.sDeviation = sDeviation;
    }

    public int doNormalDistribution() {
        return 0;
    }

    public int doPoissonDistribution() {
        return 0;
    }

    public int doEvenDistribution() {
        return 0;
    }
}
