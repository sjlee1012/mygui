package com.gmt.domain.surveillance;

public interface CoordinateConversionAlgorithm {
    /**
     * 레이더 원점 정보와 거리, 방위각을 받아
     * 타겟의 위도(lat), 경도(lon)를 반환한다.
     *
     * @param radarLat   레이더 위도
     * @param radarLon   레이더 경도
     * @param rangeKm    레이더로부터 타겟까지의 거리 (km)
     * @param bearingDeg 레이더 기준 방위각 (degrees)
     * @return double[] {targetLat, targetLon}
     */
    double[] convert(double radarLat, double radarLon, double rangeKm, double bearingDeg);
}
