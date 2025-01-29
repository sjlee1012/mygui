package com.gmt.common.iec61162.message;

public class GnsMessage extends NmeaMessage {

    private String utcTime;
    private double latitude;
    private char latDirection;
    private double longitude;
    private char lonDirection;
    private String mode;     // 예: "AA" (GPS+GLONASS)
    private int numOfSat;
    private double hdop;
    private double altitude;
    private double geoidSeparation;
    private double ageOfDiffCorr;
    private String diffStationId;

    public GnsMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $GNGNS,092750.00,4124.8963,N,00210.1324,E,AA,10,0.9,47.0,46.7,,*5C
        String[] tokens = rawSentence.split(",");
        // tokens[0] = "$GNGNS"
        // tokens[1] = "092750.00" (UTC)
        // tokens[2] = "4124.8963" (위도)
        // tokens[3] = "N"
        // tokens[4] = "00210.1324" (경도)
        // tokens[5] = "E"
        // tokens[6] = "AA" (모드)
        // tokens[7] = "10" (위성수)
        // tokens[8] = "0.9" (HDOP)
        // tokens[9] = "47.0"(고도)
        // tokens[10] = "46.7"(지오이드 분리)
        // tokens[11] = "" (diffCorrAge)
        // tokens[12] = "*5C" (체크섬 포함, diffStationId 없음)

        this.utcTime = getStringToken(tokens, 1);

        double rawLat = safeParseDouble(tokens, 2, 0.0);
        this.latDirection = getCharToken(tokens, 3, ' ');

        double rawLon = safeParseDouble(tokens, 4, 0.0);
        this.lonDirection = getCharToken(tokens, 5, ' ');

        this.mode = getStringToken(tokens, 6);
        this.numOfSat = (int) safeParseDouble(tokens, 7, 0.0);
        this.hdop = safeParseDouble(tokens, 8, 0.0);
        this.altitude = safeParseDouble(tokens, 9, 0.0);
        this.geoidSeparation = safeParseDouble(tokens, 10, 0.0);
        this.ageOfDiffCorr = safeParseDouble(tokens, 11, 0.0);
        this.diffStationId = tokens.length > 12 ? tokens[12].split("\\*")[0] : "";

        // 위도/경도 도분 → 십진수 변환
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
    private double safeParseDouble(String[] tokens, int idx, double def) {
        if (idx >= tokens.length || tokens[idx].isEmpty()) return def;
        try {
            return Double.parseDouble(tokens[idx]);
        } catch (NumberFormatException e) {
            return def;
        }
    }
    private char getCharToken(String[] tokens, int idx, char def) {
        if (idx >= tokens.length || tokens[idx].isEmpty()) return def;
        return tokens[idx].charAt(0);
    }
    private String getStringToken(String[] tokens, int idx) {
        if (idx < tokens.length) return tokens[idx];
        return "";
    }

    // Getter
    public String getUtcTime() { return utcTime; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public char getLatDirection() { return latDirection; }
    public char getLonDirection() { return lonDirection; }
    public String getMode() { return mode; }
    public int getNumOfSat() { return numOfSat; }
    public double getHdop() { return hdop; }
    public double getAltitude() { return altitude; }
    public double getGeoidSeparation() { return geoidSeparation; }
    public double getAgeOfDiffCorr() { return ageOfDiffCorr; }
    public String getDiffStationId() { return diffStationId; }
}

