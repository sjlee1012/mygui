package com.gmt.domain.surveillance;

import java.util.HashMap;
import java.util.Map;

/**
 * radar, cctv, ir 등 액티브센서를 관리하는 클래스.
 * 센서의 설치위치(위,경도)와 설치고도를 설정할 수 있음. 일단 설정하면 잘 변하지 않음
 * 센서의 방향과 거리 범위는 외부 리모트 장치를 통하여 설정이 가능함 (ptz 기능)
 */

public class ActiveSensorManager {
    private final Map<String, ActiveSensor> sensors;

    public ActiveSensorManager() {
        this.sensors = new HashMap<>();
    }

    public void addSensor(ActiveSensor sensor) {
        sensors.put(sensor.getSensorId(), sensor);
        System.out.println("Added: " + sensor);
    }

    public void removeSensor(String sensorId) {
        if (sensors.containsKey(sensorId)) {
            System.out.println("Removed: " + sensors.get(sensorId));
            sensors.remove(sensorId);
        } else {
            System.out.println("Sensor ID not found.");
        }
    }

    public ActiveSensor getSensor(String sensorId) {
        if (sensors.containsKey(sensorId)) {
            return sensors.get(sensorId);
        } else {
            System.out.println("Sensor ID not found.");
            return null;
        }
    }

    public void displayAllSensors() {
        System.out.println("All Active Sensors:");
        for (ActiveSensor sensor : sensors.values()) {
            System.out.println(sensor);
        }
    }
}
