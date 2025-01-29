package com.gmt.domain.generation.substation;


class SafetySystem {
    private FireProtectionSystem fireProtectionSystem;
    private ExplosionProofSystem explosionProofSystem;
    private LifeSavingEquipment lifeSavingEquipment;

    public SafetySystem(FireProtectionSystem fireProtectionSystem,
                        ExplosionProofSystem explosionProofSystem,
                        LifeSavingEquipment lifeSavingEquipment) {
        this.fireProtectionSystem = fireProtectionSystem;
        this.explosionProofSystem = explosionProofSystem;
        this.lifeSavingEquipment = lifeSavingEquipment;
    }
    // ...
}
