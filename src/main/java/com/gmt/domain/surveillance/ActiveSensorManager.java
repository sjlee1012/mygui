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


    public static void main(String[] args) {
        ActiveSensorManager sensorManager = new ActiveSensorManager();

        // Radar 센서 추가
        Radar radar1 = new Radar("RADAR1");
        radar1.setSensorLocation(37.7749, 122.4194, 100.0);
        radar1.setFrequency(9400);
        sensorManager.addSensor(radar1);

        // Vision Camera 센서 추가
        VisionCamera visionCamera1 = new VisionCamera("VISION1");
        visionCamera1.setSensorLocation(37.7749, 122.4194, 100.0);
        sensorManager.addSensor(visionCamera1);


        // IR Camera 센서 추가
        IRCamera irCamera1 = new IRCamera("IR1");
        irCamera1.setSensorLocation(37.7749, 122.4194, 100.0);
        sensorManager.addSensor(irCamera1);

        // 모든 센서 출력
        sensorManager.displayAllSensors();
    }


}
