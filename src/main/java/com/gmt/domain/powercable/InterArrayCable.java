package com.gmt.domain.powercable;

public class InterArrayCable {
    private String cableType;   // ex: 66kV
    private double totalLengthKm;

    public InterArrayCable(String cableType, double totalLengthKm) {
        this.cableType = cableType;
        this.totalLengthKm = totalLengthKm;
    }
    // ...
}
