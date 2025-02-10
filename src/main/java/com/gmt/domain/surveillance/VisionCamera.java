package com.gmt.domain.surveillance;


import java.time.LocalDateTime;

public class VisionCamera extends ActiveSensor {
    private Resolution resolution;
    private boolean recordingStatus;

    public VisionCamera(String sensorId) {
        super("CCTV", sensorId);
        resolution =  Resolution.HD;
        this.recordingStatus = false;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }


    public void startRecording() {
        this.recordingStatus = true;
    }

    public void stopRecording() {
        this.recordingStatus = false;
    }

    @Override
    public String toString() {
        return "CCTV Sensor(ID=" + sensorId + ", " +
                "Resolution=" + resolution.toString() +
                ", Recording=" + (recordingStatus ? "On" : "Off") +
                ")";
    }
}
