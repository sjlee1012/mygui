package com.gmt.domain.generation.substation;

class FireProtectionSystem {
    private boolean hasCo2System;
    private boolean hasWaterSpray;

    public FireProtectionSystem(boolean co2, boolean waterSpray) {
        this.hasCo2System = co2;
        this.hasWaterSpray = waterSpray;
    }
    // ...
}
