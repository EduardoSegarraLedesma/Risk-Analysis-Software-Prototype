package com.example.riskanalysissoftwareprototype.SizeEstimationItems;

import java.util.Arrays;

public class UnadjustedFPTable {

    //0 - EIrow, 1 - EOrow, 2 - EQrow, 3 - EIFrow, 4 - ILFrow;
    private int[][] table;

    private int totalTableUnadjustedFP;

    public UnadjustedFPTable() {
        table = new int[5][3];
        InitializeTable();
        totalTableUnadjustedFP = 0;
    }

    public void setvalue(int value, int row, int column) {
        table[row][column] = value;
    }

    public int getValue(int row, int column) {
        return table[row][column];
    }

    public int getTotalEIUnadjustedFP() {
        return CalculateTotalRowUnadjustedFP(0);
    }

    public int getTotalEOUnadjustedFP() {
        return CalculateTotalRowUnadjustedFP(1);
    }

    public int getTotalEQUnadjustedFP() {
        return CalculateTotalRowUnadjustedFP(2);
    }

    public int getTotalEIFUnadjustedFP() {
        return CalculateTotalRowUnadjustedFP(3);
    }

    public int getTotalILFUnadjustedFP() {
        return CalculateTotalRowUnadjustedFP(4);
    }

    public int getTotalTableUnadjustedFP() {
        UpdateTotalTableUnadjustedFP();
        return totalTableUnadjustedFP;
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private void UpdateTotalTableUnadjustedFP() {
        totalTableUnadjustedFP = CalculateTotalTableUnadjustedFP();
    }

    private int CalculateTotalTableUnadjustedFP() {
        return getTotalEIUnadjustedFP() +
                getTotalEOUnadjustedFP() +
                getTotalEQUnadjustedFP() +
                getTotalEIFUnadjustedFP() +
                getTotalILFUnadjustedFP();
    }

    private int CalculateTotalRowUnadjustedFP(int row) {
        int sum = 0;
        for (int j = 0; j < table[row].length; j++) {
            sum += table[row][j];
        }
        return sum;
    }

    private void InitializeTable() {
        for (int[] row : table) {
            Arrays.fill(row, 0);
        }
    }
}
