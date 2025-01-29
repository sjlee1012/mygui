package com.gmt.common.iec61162.message;

public class GllMessage extends NmeaMessage {

    private double latitude;       // 십진수 변환
    private char latDirection;     // 'N' or 'S'
    private double longitude;      // 십진수 변환
    private char lonDirection;     // 'E' or 'W'
    private String utcTime;        // hhmmss.ss
    private char status;           // 'A'=valid, 'V'=not valid
    private char mode;             // NMEA 2.3+ (Optional: 'A','D','E','R' 등)

    public GllMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $GPGLL,4916.45,N,12311.12,W,225444,A,A*5C
        String[] tokens = rawSentence.split(",");
        // tokens[0] = "$GPGLL"
        // tokens[1] = "4916.45" (위도)
        // tokens[2] = "N"
        // tokens[3] = "12311.12" (경도)
        // tokens[4] = "W"
        // tokens[5] = "225444" (UTC time)
        // tokens[6] = "A" (status)
        // tokens[7] = "A" (mode - optional)
        // tokens[8] = "*5C" (체크섬 포함)

        double rawLat = safeParseDouble(tokens, 1, 0.0);
        this.latDirection = getCharToken(tokens, 2, ' ');

        double rawLon = safeParseDouble(tokens, 3, 0.0);
        this.lonDirection = getCharToken(tokens, 4, ' ');

        this.utcTime = getStringToken(tokens, 5);
        this.status = getCharToken(tokens, 6, 'V');  // 기본값 'V'=invalid
        // mode (v2.3 이상)
        if (tokens.length > 7 && !tokens[7].isEmpty() && !tokens[7].contains("*")) {
            this.mode = tokens[7].charAt(0);
        } else {
            this.mode = ' ';
        }

        // 도분 → 십진수 변환
        this.latitude = convertLat(rawLat, latDirection);
        this.longitude = convertLon(rawLon, lonDirection);
    }

    private double convertLat(double val, char ns) {
        double deg = (int)(val / 100);
        double minutes = val - (deg * 100);
        double result = deg + (minutes / 60.0);
        if (ns == 'S') result = -result;
        return result;
    }
    private double convertLon(double val, char ew) {
        double deg = (int)(val / 100);
        double minutes = val - (deg * 100);
        double result = deg + (minutes / 60.0);
        if (ew == 'W') result = -result;
        return result;
    }

    private double safeParseDouble(String[] tokens, int idx, double defaultVal) {
        if (idx >= tokens.length || tokens[idx].isEmpty()) return defaultVal;
        try {
            return Double.parseDouble(tokens[idx]);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    private char getCharToken(String[] tokens, int idx, char defaultVal) {
        if (idx >= tokens.length || tokens[idx].isEmpty()) return defaultVal;
        return tokens[idx].charAt(0);
    }
    private String getStringToken(String[] tokens, int idx) {
        if (idx >= tokens.length) return "";
        return tokens[idx];
    }

    // Getter
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public char getLatDirection() { return latDirection; }
    public char getLonDirection() { return lonDirection; }
    public String getUtcTime() { return utcTime; }
    public char getStatus() { return status; }
    public char getMode() { return mode; }
}
