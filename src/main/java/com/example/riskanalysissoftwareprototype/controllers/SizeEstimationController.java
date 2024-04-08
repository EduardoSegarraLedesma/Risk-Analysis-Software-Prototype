package com.example.riskanalysissoftwareprototype.controllers;

import com.example.riskanalysissoftwareprototype.SizeEstimationItems.ComponentTypeComplexity;
import com.example.riskanalysissoftwareprototype.SizeEstimationItems.UnadjustedFPTable;

public class SizeEstimationController {

    private static SizeEstimationController instance = null;

    private UnadjustedFPTable unadjustedFPTable;

    private SizeEstimationController() {
        unadjustedFPTable = new UnadjustedFPTable();
    }

    public static SizeEstimationController getInstance() {
        if (instance == null)
            instance = new SizeEstimationController();
        return instance;
    }

    // Calculate EI Unadjusted FP
    private int CalculateEIsimpleUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EIsimple.getWeight());
    }

    private int CalculateEIaverageUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EIaverage.getWeight());
    }

    private int CalculateEIintricateUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EIintricate.getWeight());
    }

    // Calculate EO Unadjusted FP
    private int CalculateE0simpleUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EOsimple.getWeight());
    }

    private int CalculateE0averageUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EOaverage.getWeight());
    }

    private int CalculateE0intricateUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EOintricate.getWeight());
    }

    // Calculate EQ Unadjusted FP

    private int CalculateEQsimpleUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EQsimple.getWeight());
    }

    private int CalculateEQaverageUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EQaverage.getWeight());
    }

    private int CalculateEQintricateUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EQintricate.getWeight());
    }

    // Calculate EIF Unadjusted FP
    private int CalculateEIFsimpleUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EIFsimple.getWeight());
    }

    private int CalculateEIFaverageUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EIFaverage.getWeight());
    }

    private int CalculateEIFintricateUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.EIFintricate.getWeight());
    }

    // Calculate ILF Unadjusted FP
    private int CalculateILFsimpleUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.ILFsimple.getWeight());
    }

    private int CalculateILFaverageUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.ILFaverage.getWeight());
    }

    private int CalculateILFintricateUnadjustedFP(int units) {
        return CalculateUnadjustedFP(units, ComponentTypeComplexity.ILFintricate.getWeight());
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private int CalculateUnadjustedFP(int units, int weight) {
        return units * weight;
    }
}
