package com.gmt.domain.surveillance;

class CctvSystem {
    private int cameraCount;
    private boolean remoteMonitoring;

    public CctvSystem(int cameraCount, boolean remoteMonitoring) {
        this.cameraCount = cameraCount;
        this.remoteMonitoring = remoteMonitoring;
    }
}
