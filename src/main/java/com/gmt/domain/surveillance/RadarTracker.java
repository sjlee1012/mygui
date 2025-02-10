package com.gmt.domain.surveillance;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

import java.util.*;

public class RadarTracker {

    private static int targetIdCounter = 1; // 신규 타겟 ID 카운터
    private static final double MATCH_THRESHOLD = 3.0; // 타겟 일치 거리 임계값
    private static final int MAX_HISTORY = 10; // 각 타겟별 저장할 최대 이력 개수
    private static final double EARTH_RADIUS = 6371000.0; // 지구 반지름 (m)

    // 타겟 클래스 (AIS 융합 추가)
    public static class Target {
        private int id;
        private String shipID;
        private String description;
        private double vx, vy;   // 속도 (m/s)
        private LinkedList<double[]> history = new LinkedList<>(); // 위치 및 시간 이력 (최대 10개)

        private double estimationError = 1.0;
        private double processNoise = 1e-2;
        private double measurementNoise = 1e-1;

        public Target(int id, double x, double y, double timestamp, double vx, double vy) {
            this.id = id;
            this.vx = vx;
            this.vy = vy;
            addHistory(x, y, timestamp);
        }

        public void setShipID(String shipID) { this.shipID = shipID; }
        public void setDescription(String description) { this.description = description; }

        // 위치 및 시간 정보 이력 추가 (최대 10개 유지)
        private void addHistory(double x, double y, double timestamp) {
            if (history.size() >= MAX_HISTORY) {
                history.removeFirst(); // 가장 오래된 기록 삭제
            }
            history.add(new double[]{x, y, timestamp});
        }

        // 현재 위치 가져오기 (최근 기록)
        public double[] getCurrentPosition() { return history.getLast(); }

        // 칼만 필터 예측
        public void predict(double dt, double currentTime) {
            double[] lastPos = getCurrentPosition();
            double predictedX = lastPos[0] + vx * dt;
            double predictedY = lastPos[1] + vy * dt;
            addHistory(predictedX, predictedY, currentTime);
            estimationError += processNoise;
        }

        // 칼만 필터 업데이트
        public void update(double measuredX, double measuredY, double timestamp) {
            double kalmanGain = estimationError / (estimationError + measurementNoise);
            double[] lastPos = getCurrentPosition();
            double newX = lastPos[0] + kalmanGain * (measuredX - lastPos[0]);
            double newY = lastPos[1] + kalmanGain * (measuredY - lastPos[1]);
            addHistory(newX, newY, timestamp);
            estimationError *= (1 - kalmanGain);
        }

        // 두 타겟 간의 거리 계산
        public double distanceTo(Target other) {
            double[] myPos = getCurrentPosition();
            double[] otherPos = other.getCurrentPosition();
            return Math.sqrt(Math.pow(myPos[0] - otherPos[0], 2) + Math.pow(myPos[1] - otherPos[1], 2));
        }

        public String toString() {
                return this.shipID + " " + this.description + " " +
                       this.id + " " + this.vx + " " + this.vy;
        }

    }

    /**
     * AIS 데이터 클래스
     */
    public static class AisTarget {
        String shipID;
        String description;
        double lat, lon, speed, heading, timestamp;

        public AisTarget(String shipID, String description, double lat, double lon, double speed, double heading, double timestamp) {
            this.shipID = shipID;
            this.description = description;
            this.lat = lat;
            this.lon = lon;
            this.speed = speed;
            this.heading = heading;
            this.timestamp = timestamp;
        }
    }

    // 현재 추적 중인 타겟 리스트
    private static final List<Target> activeTargets = new ArrayList<>();

    /**
     * AIS 데이터를 시간 보정 (Extrapolation)
     */
    public static AisTarget correctAisPosition(AisTarget ais, double targetTime) {
        double timeDiff = targetTime - ais.timestamp;
        if (timeDiff <= 0) return ais; // 최신 데이터면 보정 불필요

        double speedMs = ais.speed * 0.514444; // knots -> m/s
        double headingRad = Math.toRadians(ais.heading);

        double newLat = ais.lat + (speedMs * timeDiff / EARTH_RADIUS) * Math.cos(headingRad);
        double newLon = ais.lon + (speedMs * timeDiff / (EARTH_RADIUS * Math.cos(Math.toRadians(ais.lat)))) * Math.sin(headingRad);

        return new AisTarget(ais.shipID, ais.description, newLat, newLon, ais.speed, ais.heading, targetTime);
    }

    /**
     * 레이더 및 AIS 타겟 융합
     */
    public static void fuseRadarAndAis(List<AisTarget> aisTargets, double currentTime) {
        List<AisTarget> correctedAisTargets = new ArrayList<>();
        for (AisTarget ais : aisTargets) {
            correctedAisTargets.add(correctAisPosition(ais, currentTime));
        }

        // 거리 행렬 계산
        int n = activeTargets.size();
        int m = correctedAisTargets.size();
        double[][] costMatrix = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double dist = activeTargets.get(i).distanceTo(new Target(0, correctedAisTargets.get(j).lat, correctedAisTargets.get(j).lon, currentTime, 0, 0));
                costMatrix[i][j] = dist;
            }
        }

        // 헝가리안 알고리즘 적용하여 최적 매칭 수행
        int[] assignments = hungarianAlgorithm(costMatrix);

        for (int i = 0; i < n; i++) {
            if (assignments[i] != -1 && costMatrix[i][assignments[i]] < MATCH_THRESHOLD) {
                Target radarTarget = activeTargets.get(i);
                AisTarget matchedAis = correctedAisTargets.get(assignments[i]);

                radarTarget.setShipID(matchedAis.shipID);
                radarTarget.setDescription(matchedAis.description);
            }
        }
    }

    public static int[] hungarianAlgorithm(double[][] costMatrix) {
        int n = costMatrix.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);

        boolean[] rowSelected = new boolean[n];
        boolean[] colSelected = new boolean[n];

        for (int i = 0; i < n; i++) {
            double minCost = Double.MAX_VALUE;
            int bestJ = -1;
            for (int j = 0; j < costMatrix[i].length; j++) {
                if (!colSelected[j] && costMatrix[i][j] < minCost) {
                    minCost = costMatrix[i][j];
                    bestJ = j;
                }
            }
            if (bestJ != -1) {
                result[i] = bestJ;
                rowSelected[i] = true;
                colSelected[bestJ] = true;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        double currentTime = 1000.0;

        activeTargets.add(new Target(1, 37.7740, -122.4190, currentTime, 10, 2));
        activeTargets.add(new Target(2, 37.7751, -122.4185, currentTime, 8, -1));
        activeTargets.add(new Target(3, 37.7760, -122.4175, currentTime, 5, 3));
        activeTargets.add(new Target(4, 37.7735, -122.4200, currentTime, 12, 1));
        activeTargets.add(new Target(5, 37.7728, -122.4210, currentTime, 7, -2));

        activeTargets.add(new Target(6, 38.7735, -123.4200, currentTime, 12, 1));
        activeTargets.add(new Target(7, 39.7728, -124.4210, currentTime, 7, -2));


        List<AisTarget> aisTargets = Arrays.asList(
                new AisTarget("AIS001", "Cargo Ship", 35.7742, -126.4192, 10.5, 178.0, 995.0),
                new AisTarget("AIS002", "Fishing Vessel", 37.7753, -122.4186, 7.8, 92.0, 990.0),
                new AisTarget("AIS003", "Tanker", 37.7762, -122.4177, 5.0, 85.0, 996.0),
                new AisTarget("AIS004", "Passenger Ship", 37.7737, -122.4202, 12.3, 175.0, 994.0),
                new AisTarget("AIS005", "Yacht", 37.7729, -122.4209, 6.5, 270.0, 993.0)
        );

        fuseRadarAndAis(aisTargets, currentTime);

        System.out.println("Fused Targets:");
        for (Target target : activeTargets) {
            System.out.println(target);
        }
    }
}
