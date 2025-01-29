package com.gmt.common.iec61162.message;

public class RmcMessage extends NmeaMessage {

    private String utcTime;       // UTC 시각 (hhmmss.ss)
    private char status;          // 'A' = valid, 'V' = warning or invalid
    private double latitude;      // 위도(십진수)
    private char latDirection;    // 'N' or 'S'
    private double longitude;     // 경도(십진수)
    private char lonDirection;    // 'E' or 'W'
    private double speedOverGround;   // SOG (노트)
    private double courseOverGround;  // COG (도)
    private String date;          // ddmmyy
    private double magneticVariation; // 자기 편차
    private char variationDirection;  // 'E' or 'W'

    public RmcMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
        String[] tokens = rawSentence.split(",");

        // tokens[0] = "$GPRMC"
        this.utcTime = getToken(tokens, 1);

        if (tokens.length > 2 && !tokens[2].isEmpty()) {
            this.status = tokens[2].charAt(0);  // 'A' or 'V'
        } else {
            this.status = 'V'; // 기본값
        }

        // 위도
        double rawLat = safeParseDouble(tokens, 3, 0.0);
        this.latDirection = (tokens.length > 4 && !tokens[4].isEmpty()) ? tokens[4].charAt(0) : ' ';

        // 경도
        double rawLon = safeParseDouble(tokens, 5, 0.0);
        this.lonDirection = (tokens.length > 6 && !tokens[6].isEmpty()) ? tokens[6].charAt(0) : ' ';

        // 속도, 코스
        this.speedOverGround = safeParseDouble(tokens, 7, 0.0);
        this.courseOverGround = safeParseDouble(tokens, 8, 0.0);

        // 날짜 (ddmmyy)
        this.date = getToken(tokens, 9);

        // 자기 편차
        this.magneticVariation = safeParseDouble(tokens, 10, 0.0);
        if (tokens.length > 11 && !tokens[11].isEmpty()) {
            this.variationDirection = tokens[11].charAt(0); // 'E' or 'W'
        } else {
            this.variationDirection = ' ';
        }

        // 위/경도 도분해 → 십진수 변환
        this.latitude = convertLat(rawLat, latDirection);
        this.longitude = convertLon(rawLon, lonDirection);
    }

    private double convertLat(double val, char ns) {
        // 예: 4807.038 → 48도 07.038분
        // 분(min) = 07.038
        // 도(deg) = 48 + (07.038 / 60)
        // 북/남에 따라 부호 결정
        double degrees = (int)(val / 100);
        double minutes = val - (degrees * 100);
        double decimal = degrees + (minutes / 60.0);
        if (ns == 'S') {
            decimal = -decimal;
        }
        return decimal;
    }
    private double convertLon(double val, char ew) {
        double degrees = (int)(val / 100);
        double minutes = val - (degrees * 100);
        double decimal = degrees + (minutes / 60.0);
        if (ew == 'W') {
            decimal = -decimal;
        }
        return decimal;
    }

    private String getToken(String[] tokens, int idx) {
        if (idx < tokens.length) {
            return tokens[idx];
        }
        return "";
    }

    private double safeParseDouble(String[] tokens, int idx, double defaultVal) {
        if (idx >= tokens.length || tokens[idx].isEmpty()) return defaultVal;
        try {
            return Double.parseDouble(tokens[idx]);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    // Getter
    public String getUtcTime() {
        return utcTime;
    }
    public char getStatus() {
        return status;
    }
    public double getLatitude() {
        return latitude;
    }
    public char getLatDirection() {
        return latDirection;
    }
    public double getLongitude() {
        return longitude;
    }
    public char getLonDirection() {
        return lonDirection;
    }
    public double getSpeedOverGround() {
        return speedOverGround;
    }
    public double getCourseOverGround() {
        return courseOverGround;
    }
    public String getDate() {
        return date;
    }
    public double getMagneticVariation() {
        return magneticVariation;
    }
    public char getVariationDirection() {
        return variationDirection;
    }
}

