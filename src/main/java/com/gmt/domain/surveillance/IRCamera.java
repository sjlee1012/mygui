package com.gmt.domain.surveillance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IRCamera extends ActiveSensor {
    private double thermalSensitivity;
    private final List<String> detectedHeatSources;

    public IRCamera(double latitude, double longitude, double altitude) {
        super("IR Camera", latitude, longitude, altitude);
        this.detectedHeatSources = new ArrayList<>();
    }

    public void detectHeatSource(String sourceId) {
        detectedHeatSources.add(sourceId);
        this.lastUpdated = LocalDateTime.now();
    }

    public List<String> getDetectedHeatSources() {
        return detectedHeatSources;
    }

    @Override
    public String toString() {
        return "IR Camera Sensor(ID=" + sensorId + ", Sensitivity=" + thermalSensitivity + ", Heat Sources=" + detectedHeatSources.size() + ")";
    }
}
