package com.gmt.domain.generation.structure;

public class GravityFoundation extends Foundation {
    private double baseDiameter;
    private double totalWeight;

    public GravityFoundation(double waterDepth, double baseDiameter, double totalWeight) {
        super("Gravity", waterDepth);
        this.baseDiameter = baseDiameter;
        this.totalWeight = totalWeight;
    }
    // ...
}
