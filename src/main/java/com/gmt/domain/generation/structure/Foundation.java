package com.gmt.domain.generation.structure;

public abstract class Foundation {
    protected String foundationType;
    protected double waterDepth;
    // etc.

    public Foundation(String foundationType, double waterDepth) {
        this.foundationType = foundationType;
        this.waterDepth = waterDepth;
    }

    // common methods
}
