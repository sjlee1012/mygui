package com.gmt.common.iec61162.message;

public class GgaMessage extends NmeaMessage {
    private String time;
    private double latitude;
    private double longitude;
    private int fixQuality;
    private int satelliteCount;
    private double altitude;

    public GgaMessage(String rawSentence) {
        super(rawSentence);
        parseFields();  // 생성자에서 바로 필드 파싱
    }

    @Override
    public void parseFields() {
        // 예: $GPGGA,123456.00,3714.2803,N,12701.5260,E,1,08,1.09,117.2,M,17.8,M,,*5B
        String[] tokens = rawSentence.split(",");
        // tokens[0] = "$GPGGA"
        this.time = tokens[1];

        // 위도(토큰[2], N/S: 토큰[3])
        this.latitude = parseLat(tokens[2], tokens[3]);

        // 경도(토큰[4], E/W: 토큰[5])
        this.longitude = parseLon(tokens[4], tokens[5]);

        // fixQuality(토큰[6]), satelliteCount(토큰[7]), altitude(토큰[9]) 등
        this.fixQuality = Integer.parseInt(tokens[6]);
        this.satelliteCount = Integer.parseInt(tokens[7]);
        this.altitude = Double.parseDouble(tokens[9]);
    }

    private double parseLat(String latStr, String ns) {
        // ddmm.mmmm → 도 단위로 변환 (간단 예시)
        double val = Double.parseDouble(latStr) / 100.0;
        if ("S".equals(ns)) {
            val = -val;
        }
        return val;
    }
    private double parseLon(String lonStr, String ew) {
        double val = Double.parseDouble(lonStr) / 100.0;
        if ("W".equals(ew)) {
            val = -val;
        }
        return val;
    }

    public String getTime() {
        return time;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public int getFixQuality() {
        return fixQuality;
    }
    public int getSatelliteCount() {
        return satelliteCount;
    }
    public double getAltitude() {
        return altitude;
    }
}
