package com.gmt.domain.generation.substation;



import java.util.List;

class PowerSystem {
    private Transformer mainTransformer;
    private Switchgear switchgear; // GIS/AIS
    private List<ProtectionRelay> protectionRelays;
    private double substationVoltageLevel; // ex: 220kV

    public PowerSystem(Transformer mainTransformer,
                       Switchgear switchgear,
                       List<ProtectionRelay> relays,
                       double voltageLevel) {
        this.mainTransformer = mainTransformer;
        this.switchgear = switchgear;
        this.protectionRelays = relays;
        this.substationVoltageLevel = voltageLevel;
    }
    // ...
}
