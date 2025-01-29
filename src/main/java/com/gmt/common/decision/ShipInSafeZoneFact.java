package com.gmt.common.decision;

/**
 * Drools 룰 처리를 위한 "선박-안전영역 상태" Fact
 * - 언제 진입했는지 (enterTime)
 * - 아직도 영역 안에 머무르고 있는지 (inside)
 */
public class ShipInSafeZoneFact {
    private String shipId;
    private String zoneId;
    private long enterTime;  // 밀리초 단위( System.currentTimeMillis() )
    private boolean inside;  // true=진입 상태, false=이탈 상태

    public ShipInSafeZoneFact(String shipId, String zoneId, long enterTime, boolean inside) {
        this.shipId = shipId;
        this.zoneId = zoneId;
        this.enterTime = enterTime;
        this.inside = inside;
    }

    public String getShipId() {
        return shipId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public long getEnterTime() {
        return enterTime;
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }

    public void setEnterTime(long enterTime) {
        this.enterTime = enterTime;
    }

    @Override
    public String toString() {
        return "ShipInSafeZoneFact{" +
                "shipId='" + shipId + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", enterTime=" + enterTime +
                ", inside=" + inside +
                '}';
    }
}

