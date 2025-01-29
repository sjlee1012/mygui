package com.gmt.domain.generation.substation;

abstract class Switchgear {
    protected String type; // GIS / AIS

    public Switchgear(String type) {
        this.type = type;
    }
}
