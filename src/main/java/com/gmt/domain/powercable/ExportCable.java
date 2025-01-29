package com.gmt.domain.powercable;

public class ExportCable {
    private String cableType;   // ex: 220kV XLPE
    private double lengthKm;

    public ExportCable(String cableType, double lengthKm) {
        this.cableType = cableType;
        this.lengthKm = lengthKm;
    }
    // ...
}
