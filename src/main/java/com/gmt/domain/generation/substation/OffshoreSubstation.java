package com.gmt.domain.generation.substation;

import com.gmt.common.scada.ScadaSystem;
import com.gmt.common.ship.InstallationEquipment;
import com.gmt.domain.generation.structure.Structure;

/**
 * 고정식 해상 변전소 (Offshore Substation) 메인 클래스
 */
public class OffshoreSubstation {

    private String substationName;
    private Structure structure;        // 하부 구조 및 플랫폼
    private PowerSystem powerSystem;    // 전력설비
    private CableSystem cableSystem;    // 케이블
    private ScadaSystem scadaSystem;    // SCADA
    private SafetySystem safetySystem;  // 안전/부수 설비
    private InstallationEquipment installationEquipment; // 설치/운송 장비 등

    public OffshoreSubstation(String substationName,
                              Structure structure,
                              PowerSystem powerSystem,
                              CableSystem cableSystem,
                              ScadaSystem scadaSystem,
                              SafetySystem safetySystem,
                              InstallationEquipment installationEquipment) {
        this.substationName = substationName;
        this.structure = structure;
        this.powerSystem = powerSystem;
        this.cableSystem = cableSystem;
        this.scadaSystem = scadaSystem;
        this.safetySystem = safetySystem;
        this.installationEquipment = installationEquipment;
    }

    // getters / setters / toString ...
}
