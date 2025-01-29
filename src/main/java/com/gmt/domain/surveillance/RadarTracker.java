package com.gmt.domain.surveillance;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

public class RadarTracker {

    private static int targetIdCounter = 1; // 신규 타겟 ID 카운터
    private static final double MATCH_THRESHOLD = 3.0; // 타겟 일치 거리 임계값
    private static final int MAX_HISTORY = 10; // 각 타겟별 저장할 최대 이력 개수

    // 타겟 클래스 (시간 정보 추가)
    public static class Target {
        private int id;
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

        // 위치 및 시간 정보 이력 추가 (최대 10개 유지)
        private void addHistory(double x, double y, double timestamp) {
            if (history.size() >= MAX_HISTORY) {
                history.removeFirst(); // 가장 오래된 기록 삭제
            }
            history.add(new double[]{x, y, timestamp});
        }

        // 현재 위치 가져오기 (최근 기록)
        public double[] getCurrentPosition() {
            return history.getLast();
        }

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

        // 두 타겟 간의 거리 계산 (현재 위치 비교)
        public double distanceTo(Target other) {
            double[] myPos = getCurrentPosition();
            double[] otherPos = other.getCurrentPosition();
            return Math.sqrt(Math.pow(myPos[0] - otherPos[0], 2) + Math.pow(myPos[1] - otherPos[1], 2));
        }

        // 특정 시간 범위의 위치 이력 조회
        public List<double[]> getHistoryInRange(double startTime, double endTime) {
            List<double[]> result = new ArrayList<>();
            for (double[] record : history) {
                if (record[2] >= startTime && record[2] <= endTime) {
                    result.add(record);
                }
            }
            return result;
        }

        @Override
        public String toString() {
            double[] pos = getCurrentPosition();
            return String.format("Target(ID=%d, X=%.2f, Y=%.2f, VX=%.2f, VY=%.2f, Time=%.2f, History=%d)",
                    id, pos[0], pos[1], vx, vy, pos[2], history.size());
        }
    }

    // 현재 추적 중인 타겟 리스트
    private static final List<Target> activeTargets = new ArrayList<>();

    public static void trackTargets(List<Target> newMeasurements, double dt, double currentTime) {
        // 기존 타겟 위치 예측 (칼만 필터 예측 단계)
        for (Target target : activeTargets) {
            target.predict(dt, currentTime);
        }

        // 거리 행렬 계산 (예측된 위치 vs 새로운 측정값)
        int n = activeTargets.size();
        int m = newMeasurements.size();
        double[][] costMatrix = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                costMatrix[i][j] = activeTargets.get(i).distanceTo(newMeasurements.get(j));
            }
        }

        // 헝가리안 알고리즘 적용하여 최적의 타겟 매칭 수행
        int[] assignments = hungarianAlgorithm(costMatrix);

        // 매칭된 타겟 업데이트
        boolean[] matchedNewTargets = new boolean[m];
        for (int i = 0; i < n; i++) {
            if (assignments[i] != -1 && costMatrix[i][assignments[i]] < MATCH_THRESHOLD) {
                Target assignedMeasurement = newMeasurements.get(assignments[i]);
                activeTargets.get(i).update(assignedMeasurement.getCurrentPosition()[0], assignedMeasurement.getCurrentPosition()[1], currentTime);
                matchedNewTargets[assignments[i]] = true;
            }
        }

        // 매칭되지 않은 새로운 측정값을 신규 타겟으로 추가
        for (int j = 0; j < m; j++) {
            if (!matchedNewTargets[j]) {
                Target newTarget = newMeasurements.get(j);
                newTarget.id = targetIdCounter++;
                activeTargets.add(newTarget);
            }
        }

        // 결과 출력
        System.out.println("Updated Active Targets:");
        for (Target target : activeTargets) {
            System.out.println(target);
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
        double currentTime = 100.0; // 예제에서 100초부터 시작
        List<Target> newMeasurements = Arrays.asList(
                new Target(0, 5.9, 5.4, currentTime, 0.2, 0.1),
                new Target(0, 9.6, 10.8, currentTime, -0.1, 0.2),
                new Target(0, 20.0, 20.0, currentTime, 0.0, 0.0)
        );

        double dt = 1.0; // 1초 후의 관측값 가정
        trackTargets(newMeasurements, dt, currentTime);


        // 특정 타겟 ID와 시간 범위로 이력 조회 예제
        int targetId = 1;
        double startTime = 95.0, endTime = 105.0;
        for (Target target : activeTargets) {
            if (target.id == targetId) {
                List<double[]> history = target.getHistoryInRange(startTime, endTime);
                System.out.println("History for Target ID " + targetId + " from " + startTime + " to " + endTime + ":");
                for (double[] record : history) {
                    System.out.println(Arrays.toString(record));
                }
            }
        }
    }
}
