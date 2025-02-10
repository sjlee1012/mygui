package com.gmt.domain.surveillance;

public class ShipPositionCalculator {

    // WGS-84 타원체 반지름 값
    private static final double EQUATORIAL_RADIUS = 6378137.0; // 적도 반지름 (m)
    private static final double POLAR_RADIUS = 6356752.3142;   // 극지방 반지름 (m)

    /**
     * 카메라의 위치 및 각도를 기반으로 선박의 거리, 방위각, 위경도를 계산
     *
     * @param cameraLat  카메라 위도 (degrees)
     * @param cameraLon  카메라 경도 (degrees)
     * @param cameraAlt  카메라 고도 (meters)
     * @param bearing    방위각 (degrees, 0°=북쪽, 90°=동쪽)
     * @param elevation  수평각 (degrees, 0°=수평선, 90°=정면, 음수=아래 방향)
     * @param useWGS84   WGS-84 타원체 모델 사용 여부 (true: 사용, false: 기본 구 모델)
     * @return 선박의 거리(m), 위도, 경도를 포함한 배열 [거리, 위도, 경도]
     */
    public static double[] calculateShipPosition(double cameraLat, double cameraLon, double cameraAlt, double bearing, double elevation, boolean useWGS84) {
        // 선택한 지구 반지름(R) 계산
        double earthRadius = useWGS84 ? getWGS84Radius(cameraLat) : EQUATORIAL_RADIUS;

        // 수평각을 이용하여 거리 계산 (삼각법 적용)
        double distance;
        if (elevation >= 90) {
            distance = Double.POSITIVE_INFINITY; // 정면을 향하면 거리 무한대 처리
        } else if (elevation <= -10) {
            distance = 0; // 너무 아래를 보면 거리 0 처리
        } else {
            distance = cameraAlt * Math.tan(Math.toRadians(90 - elevation));
        }

        // 방위각을 라디안으로 변환
        double bearingRad = Math.toRadians(bearing);

        // 위도를 라디안으로 변환
        double cameraLatRad = Math.toRadians(cameraLat);
        double cameraLonRad = Math.toRadians(cameraLon);

        // 위도 변화 계산
        double newLatRad = cameraLatRad + (distance / earthRadius) * Math.cos(bearingRad);
        double newLat = Math.toDegrees(newLatRad);

        // 경도 변화 계산
        double newLonRad = cameraLonRad + (distance / (earthRadius * Math.cos(cameraLatRad))) * Math.sin(bearingRad);
        double newLon = Math.toDegrees(newLonRad);

        return new double[]{distance, newLat, newLon};
    }

    /**
     * WGS-84 모델을 사용하여 특정 위도에서의 지구 반지름 계산
     *
     * @param latitude 위도 (degrees)
     * @return 해당 위도에서의 지구 반지름 (meters)
     */
    private static double getWGS84Radius(double latitude) {
        double latRad = Math.toRadians(latitude);
        double cosLat = Math.cos(latRad);
        double sinLat = Math.sin(latRad);

        double numerator = Math.pow(EQUATORIAL_RADIUS * cosLat, 2) + Math.pow(POLAR_RADIUS * sinLat, 2);
        double denominator = Math.pow(EQUATORIAL_RADIUS * cosLat, 2) + Math.pow(POLAR_RADIUS * sinLat, 2);

        return Math.sqrt(numerator / denominator);
    }

    public static void main(String[] args) {
        // 카메라 위치 (위도, 경도, 고도)
        double cameraLat = 37.7749; // 위도 (예: 샌프란시스코)
        double cameraLon = -122.4194; // 경도
        double cameraAlt = 50.0; // 카메라의 고도 (50m)

        // 선박의 방위각 및 수평각
        double bearing = 45.0; // 방위각 (도)
        double elevation = 5.0; // 수평각 (도) - 예: 선박이 살짝 위쪽에 있음

        // WGS-84 사용 여부
        boolean useWGS84 = true;

        // 선박의 위치 계산
        double[] shipData = calculateShipPosition(cameraLat, cameraLon, cameraAlt, bearing, elevation, useWGS84);

        System.out.printf("WGS-84 적용 여부: %s%n", useWGS84 ? "적용" : "미적용");
        System.out.printf("선박의 거리: %.2f m%n", shipData[0]);
        System.out.printf("선박의 위도: %.6f, 경도: %.6f%n", shipData[1], shipData[2]);
    }
}
