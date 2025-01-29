package com.gmt.common.iec61162.message;

public class MwvMessage extends NmeaMessage {

    private double windAngle;
    private char reference;      // 'R' or 'T'
    private double windSpeed;
    private char speedUnit;      // 'N','M','K', etc.
    private char status;         // 'A' or 'V'

    public MwvMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $IIMWV,045,R,10.5,N,A*3C
        // tokens[0] = "$IIMWV"
        // tokens[1] = "045"    (windAngle)
        // tokens[2] = "R"      (reference)
        // tokens[3] = "10.5"   (windSpeed)
        // tokens[4] = "N"      (speedUnit)
        // tokens[5] = "A"      (status)
        // tokens[6] = "*3C"    (체크섬 포함)
        String[] tokens = rawSentence.split(",");

        this.windAngle = safeParseDouble(tokens, 1, 0.0);

        if (tokens.length > 2 && !tokens[2].isEmpty()) {
            this.reference = tokens[2].charAt(0); // 'R' or 'T'
        } else {
            this.reference = ' ';
        }

        this.windSpeed = safeParseDouble(tokens, 3, 0.0);

        if (tokens.length > 4 && !tokens[4].isEmpty()) {
            this.speedUnit = tokens[4].charAt(0);
        } else {
            this.speedUnit = ' ';
        }

        if (tokens.length > 5 && !tokens[5].isEmpty()) {
            this.status = tokens[5].charAt(0);  // 'A' or 'V'
        } else {
            this.status = ' ';
        }

        // 필요 시 체크섬 검증
    }

    // 안전한 double 파싱
    private double safeParseDouble(String[] tokens, int idx, double defaultVal) {
        if (idx >= tokens.length) return defaultVal;
        String val = tokens[idx];
        if (val == null || val.isEmpty()) return defaultVal;
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    // Getter
    public double getWindAngle() {
        return windAngle;
    }
    public char getReference() {
        return reference;
    }
    public double getWindSpeed() {
        return windSpeed;
    }
    public char getSpeedUnit() {
        return speedUnit;
    }
    public char getStatus() {
        return status;
    }
}
