package com.gmt.common.geo;

import org.locationtech.jts.geom.LineString;

/**
 * 안전항로
 * - 라인스트링 + 폭(width)을 이용해 buffer() -> Polygon
 */
public class SafeRoute extends Region {

    private LineString routeLine; // 실제 라인스트링 (기록용)
    private double routeWidth;

    /**
     * 생성자에서 lineString을 buffer하여 Polygon geometry로 보관
     */
    public SafeRoute(String id, LineString routeLine, double routeWidth) {
        super(id, routeLine.buffer(routeWidth / 2.0));
        this.routeLine = routeLine;
        this.routeWidth = routeWidth;
    }

    public LineString getRouteLine() {
        return routeLine;
    }

    public double getRouteWidth() {
        return routeWidth;
    }
}
