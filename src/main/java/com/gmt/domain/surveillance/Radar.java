package com.gmt.domain.surveillance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Radar extends ActiveSensor {
    private double frequency;
    private final List<String> detectedTargets;

    public Radar(String sensorId) {
        super("RADAR", sensorId);
        this.detectedTargets = new ArrayList<>();
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void detectTarget(String targetId) {
        detectedTargets.add(targetId);
    }

    public List<String> getDetectedTargets() {
        return detectedTargets;
    }

    @Override
    public String toString() {
        return "Radar Sensor(ID=" + sensorId + ", Frequency=" + frequency + "GHz, Targets=" + detectedTargets.size() + ")";
    }
}
