package com.gmt.domain.generation.substation;

/* ======================== CABLE SYSTEM ======================== */

import com.gmt.domain.powercable.ExportCable;
import com.gmt.domain.powercable.InterArrayCable;

public class CableSystem {
    private ExportCable exportCable;
    private InterArrayCable interArrayCable;
    private boolean hasFiberOptics;

    public CableSystem(ExportCable exportCable, InterArrayCable interArrayCable, boolean hasFiberOptics) {
        this.exportCable = exportCable;
        this.interArrayCable = interArrayCable;
        this.hasFiberOptics = hasFiberOptics;
    }
    // ...
}
