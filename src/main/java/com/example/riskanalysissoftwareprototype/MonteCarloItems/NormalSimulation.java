package com.example.riskanalysissoftwareprototype.MonteCarloItems;

import org.apache.commons.math3.distribution.LogNormalDistribution;

public class NormalSimulation extends MonteCarloSimulation implements IMonteCarloSimulation {
    public NormalSimulation(int mOptimistic, int mLikely, int mPessimistic, int mDuration, int sDeviation) {
        super(mOptimistic, mLikely, mPessimistic, mDuration, sDeviation);
    }

    @Override
    public int doDistribution() {
        double variance = sDeviation * sDeviation;
        double mu = Math.log((mDuration * mDuration) / Math.sqrt(variance + mDuration * mDuration));
        double sigma = Math.sqrt(Math.log(variance / (mDuration * mDuration) + 1));
        LogNormalDistribution distribution = new LogNormalDistribution(mu, sigma);
        return (int) Math.round(distribution.sample());
    }
}
