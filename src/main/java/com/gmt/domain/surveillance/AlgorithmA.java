package com.gmt.domain.surveillance;

public class AlgorithmA implements CoordinateConversionAlgorithm {
    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public double[] convert(double radarLat, double radarLon, double rangeKm, double bearingDeg) {
        // 각도 -> 라디안 변환
        double latRad = Math.toRadians(radarLat);
        double lonRad = Math.toRadians(radarLon);
        double bearingRad = Math.toRadians(bearingDeg);

        // 구면(지구)에서의 간단한 대원거리(Great-circle) 이동 공식
        double angularDistance = rangeKm / EARTH_RADIUS_KM; // 중심각(라디안)

        double targetLatRad = Math.asin(
                Math.sin(latRad) * Math.cos(angularDistance)
                        + Math.cos(latRad) * Math.sin(angularDistance) * Math.cos(bearingRad)
        );

        double targetLonRad = lonRad + Math.atan2(
                Math.sin(bearingRad) * Math.sin(angularDistance) * Math.cos(latRad),
                Math.cos(angularDistance) - Math.sin(latRad) * Math.sin(targetLatRad)
        );

        double targetLat = Math.toDegrees(targetLatRad);
        double targetLon = Math.toDegrees(targetLonRad);

        return new double[]{targetLat, targetLon};
    }
}
