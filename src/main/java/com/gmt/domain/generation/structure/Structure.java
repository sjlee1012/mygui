package com.gmt.domain.generation.structure;


public class Structure {
    private Foundation foundation;  // 기초 (Jacket/Gravity/Monopile)
    private Platform platform;      // 플랫폼(Deck, Helideck 등)

    public Structure(Foundation foundation, Platform platform) {
        this.foundation = foundation;
        this.platform = platform;
    }

    // getters, setters, toString...
}
