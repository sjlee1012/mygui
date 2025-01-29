package com.gmt.domain.generation.substation;

class ProtectionRelay {
    private String relayName; // 거리계전기, 차동계전기 등
    private String function;  // OC, Distance, Diff...

    public ProtectionRelay(String relayName, String function) {
        this.relayName = relayName;
        this.function = function;
    }
}
