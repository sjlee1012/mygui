package com.gmt.domain.surveillance;

import java.util.UUID;
import java.time.LocalDateTime;

public abstract class ActiveSensor {
    protected String sensorId;
    protected String sensorType;
    protected double latitude;
    protected double longitude;
    protected double altitude;

    protected double direction;
    protected double rangeKm;
    protected String status;
    protected LocalDateTime lastUpdated;

    public ActiveSensor(String sensorType, double latitude, double longitude, double altitude) {
        this.sensorId = UUID.randomUUID().toString();
        this.sensorType = sensorType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.status = "active";
        this.lastUpdated = LocalDateTime.now();
    }

    public void setDirection(double direction) {
        this.direction = direction;
        this.lastUpdated = LocalDateTime.now();
    }

    public void setRangeKm(double rangeKm) {
        this.rangeKm = rangeKm;
    }

    public void setStatus(String status) {
        if (status.equals("active") || status.equals("inactive")) {
            this.status = status;
            this.lastUpdated = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return sensorType + " Sensor(ID=" + sensorId + ", Location=(" + latitude + ", " + longitude + "), Status=" + status + ")";
    }

    public String getSensorId() {
        return sensorId;
    }
}
