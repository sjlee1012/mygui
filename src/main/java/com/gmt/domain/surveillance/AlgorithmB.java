package com.gmt.domain.surveillance;

public class AlgorithmB implements CoordinateConversionAlgorithm {
    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public double[] convert(double radarLat, double radarLon, double rangeKm, double bearingDeg) {
        // 각도 -> 라디안 변환
        double latRad = Math.toRadians(radarLat);
        double bearingRad = Math.toRadians(bearingDeg);

        // 단거리(수 km 내) 가정: x, y 평면 근사
        // Δlat = (range / R) * cos(bearing)
        // Δlon = (range / R) * sin(bearing) / cos(lat0)
        double dLat = (rangeKm / EARTH_RADIUS_KM) * Math.cos(bearingRad);
        double dLon = (rangeKm / EARTH_RADIUS_KM) * Math.sin(bearingRad) / Math.cos(latRad);

        double targetLat = radarLat + Math.toDegrees(dLat);
        double targetLon = radarLon + Math.toDegrees(dLon);

        return new double[]{targetLat, targetLon};
    }
}

