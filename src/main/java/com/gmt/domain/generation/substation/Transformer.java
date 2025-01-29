package com.gmt.domain.generation.substation;

public class Transformer {
    private String model;
    private double capacityMVA;
    // 냉각방식, 권선비 등등

    public Transformer(String model, double capacityMVA) {
        this.model = model;
        this.capacityMVA = capacityMVA;
    }
    // ...
}
