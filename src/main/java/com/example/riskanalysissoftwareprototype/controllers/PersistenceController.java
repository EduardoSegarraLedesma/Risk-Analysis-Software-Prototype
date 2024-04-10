package com.example.riskanalysissoftwareprototype.controllers;

public class PersistenceController {
    private static PersistenceController instance = null;

    private PersistenceController() {
    }

    public static PersistenceController getInstance() {
        if (instance == null)
            instance = new PersistenceController();
        return instance;
    }
}
