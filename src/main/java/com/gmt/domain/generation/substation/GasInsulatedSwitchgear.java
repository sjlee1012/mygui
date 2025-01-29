package com.gmt.domain.generation.substation;


public class GasInsulatedSwitchgear extends Switchgear {
    private String gasType; // SF6 등

    public GasInsulatedSwitchgear(String gasType) {
        super("GIS");
        this.gasType = gasType;
    }
}
