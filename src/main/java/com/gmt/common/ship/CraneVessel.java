package com.gmt.common.ship;

class CraneVessel {
    private String model;
    private double maxLiftCapacity; // ton

    public CraneVessel(String model, double maxLiftCapacity) {
        this.model = model;
        this.maxLiftCapacity = maxLiftCapacity;
    }
}
