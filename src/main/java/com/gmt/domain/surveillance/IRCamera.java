package com.gmt.domain.surveillance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IRCamera extends ActiveSensor {
    private double thermalSensitivity;
    private final List<String> detectedHeatSources;

    public IRCamera(String sensorId) {
        super("IRCamera", sensorId);
        this.detectedHeatSources = new ArrayList<>();
    }

    public void detectHeatSource(String sourceId) {
        detectedHeatSources.add(sourceId);
    }

    public List<String> getDetectedHeatSources() {
        return detectedHeatSources;
    }

    @Override
    public String toString() {
        return "IR Camera Sensor(ID=" + sensorId + ", Sensitivity=" + thermalSensitivity + ", Heat Sources=" + detectedHeatSources.size() + ")";
    }
}
