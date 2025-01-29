package com.gmt.domain.generation.structure;

class Platform {
    private String platformType; // 예: 단층/복층, helideck 포함
    private double deckHeight;
    private double designLoad;

    public Platform(String platformType, double deckHeight, double designLoad) {
        this.platformType = platformType;
        this.deckHeight = deckHeight;
        this.designLoad = designLoad;
    }
    // ...
}
