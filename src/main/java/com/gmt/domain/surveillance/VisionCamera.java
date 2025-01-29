package com.gmt.domain.surveillance;


import java.time.LocalDateTime;

public class VisionCamera extends ActiveSensor {
    private Resolution resolution;
    private boolean recordingStatus;

    public VisionCamera(double latitude, double longitude, double direction) {
        super("CCTV", latitude, longitude, direction);
        this.recordingStatus = false;
    }

    public void startRecording() {
        this.recordingStatus = true;
        this.lastUpdated = LocalDateTime.now();
    }

    public void stopRecording() {
        this.recordingStatus = false;
        this.lastUpdated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "CCTV Sensor(ID=" + sensorId + ", " +
                "Resolution=" + resolution.toString() +
                ", Recording=" + (recordingStatus ? "On" : "Off") +
                ")";
    }
}
