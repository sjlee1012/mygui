package com.gmt.domain.generation.substation;


import com.gmt.domain.generation.structure.Foundation;

public class JacketFoundation extends Foundation {
    private int legCount;    // 재킷 기둥 수
    private double pileDepth;

    public JacketFoundation(double waterDepth, int legCount, double pileDepth) {
        super("Jacket", waterDepth);
        this.legCount = legCount;
        this.pileDepth = pileDepth;
    }
    // ...
}
