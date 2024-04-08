package com.example.riskanalysissoftwareprototype.SizeEstimationItems;

public enum  ComponentTypeComplexity {

    EIsimple(3),  // External Input
    EIaverage(4),
    EIintricate(6),
    EOsimple(4),  // External Output
    EOaverage(5),
    EOintricate(7),
    EQsimple(3),  // External Inquiry
    EQaverage(4),
    EQintricate(6),
    EIFsimple(5), // External Interface File
    EIFaverage(7),
    EIFintricate(10),
    ILFsimple(7), // Internal Logical File
    ILFaverage(10),
    ILFintricate(15);


    private final int weight;

    ComponentTypeComplexity( int weight){
        this.weight = weight;
    }

    // Getter method to retrieve the value
    public int getWeight() {
        return this.weight;
    }
}
