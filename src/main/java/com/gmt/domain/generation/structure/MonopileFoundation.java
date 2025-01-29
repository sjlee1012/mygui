package com.gmt.domain.generation.structure;

public class MonopileFoundation extends Foundation {
    private double monopileDiameter;
    private double penetrationDepth;

    public MonopileFoundation(double waterDepth, double monopileDiameter, double penetrationDepth) {
        super("Monopile", waterDepth);
        this.monopileDiameter = monopileDiameter;
        this.penetrationDepth = penetrationDepth;
    }
    // ...
}
