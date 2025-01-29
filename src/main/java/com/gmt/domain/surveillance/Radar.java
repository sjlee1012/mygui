package com.gmt.domain.surveillance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Radar extends ActiveSensor {
    private double frequency;
    private final List<String> detectedTargets;

    public Radar(double latitude, double longitude, double altitude) {
        super("Radar", latitude, longitude, altitude);
        this.detectedTargets = new ArrayList<>();
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void detectTarget(String targetId) {
        detectedTargets.add(targetId);
        this.lastUpdated = LocalDateTime.now();
    }

    public List<String> getDetectedTargets() {
        return detectedTargets;
    }

    @Override
    public String toString() {
        return "Radar Sensor(ID=" + sensorId + ", Frequency=" + frequency + "GHz, Targets=" + detectedTargets.size() + ")";
    }
}
