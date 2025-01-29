package com.gmt.common.geo;

import org.locationtech.jts.geom.Geometry;

/**
 * 풍력단지 내의 각 영역(위험영역, 안전항로 등)을 추상화
 */
public abstract class Region {
    protected String id;
    protected Geometry geometry; // JTS Geometry

    public Region(String id, Geometry geometry) {
        this.id = id;
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public String toString() {
        return String.format("%s{id='%s', geometry=%s}",
                getClass().getSimpleName(), id, geometry.toText());
    }
}

