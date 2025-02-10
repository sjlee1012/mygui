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

    public ActiveSensor(String sensorType, String sensorId) {
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.status = "active";
    }

    public void setSensorLocation(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setRangeKm(double rangeKm) {
        this.rangeKm = rangeKm;
    }

    public void setStatus(String status) {
        if (status.equals("active") || status.equals("inactive")) {
            this.status = status;
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
