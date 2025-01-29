package com.gmt.common.scada;

public class ScadaSystem {
    private ScadaSoftware scadaSoftware;
    private CommProtocol commProtocol;
    private boolean hasRemoteControl;
    private Hmi hmi;

    public ScadaSystem(ScadaSoftware scadaSoftware,
                       CommProtocol commProtocol,
                       boolean hasRemoteControl,
                       Hmi hmi) {
        this.scadaSoftware = scadaSoftware;
        this.commProtocol = commProtocol;
        this.hasRemoteControl = hasRemoteControl;
        this.hmi = hmi;
    }
    // ...
}
