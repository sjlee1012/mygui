package com.gmt.common.geo;


import org.locationtech.jts.geom.Geometry;

/**
 * 위험영역
 * 내부 geometry 는 주로 Polygon 또는 Circle(Polygon approximation) 등을 사용
 */
public class DangerZone extends Region {

    public DangerZone(String id, Geometry geometry) {
        super(id, geometry);
    }

    // 필요 시 추가 로직(위험도 등)
}

