package com.gmt.common.ship;

public class InstallationEquipment {
    private CraneVessel craneVessel;
    private TransportBarge transportBarge;
    private boolean hasHelideck;
    private boolean hasROV;

    public InstallationEquipment(CraneVessel craneVessel, TransportBarge transportBarge,
                                 boolean hasHelideck, boolean hasROV) {
        this.craneVessel = craneVessel;
        this.transportBarge = transportBarge;
        this.hasHelideck = hasHelideck;
        this.hasROV = hasROV;
    }
    // ...
}
