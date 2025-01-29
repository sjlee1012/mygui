package com.gmt.common.iec61162.message;

public class HdgMessage extends NmeaMessage {

    private double heading;            // 자침 방위(Heading)
    private double magneticDeviation;  // 편차(Deviation)
    private char deviationDirection;   // 편차 방향('E' or 'W')
    private double magneticVariation;  // 자차(Variation)
    private char variationDirection;   // 자차 방향('E' or 'W')

    public HdgMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $HCHDG,101.1,5.5,E,22.4,W*34
        // 토큰 분리
        // tokens[0] = "$HCHDG"
        String[] tokens = rawSentence.split(",");

        // 토큰 인덱스 별로 파싱 (장치/문자열 형식에 따라 달라질 수 있음)
        // Heading
        this.heading = safeParseDouble(tokens[1], 0.0);

        // Magnetic Deviation
        this.magneticDeviation = safeParseDouble(tokens[2], 0.0);

        // Deviation Direction (E/W 등)
        if (tokens.length > 3 && tokens[3].length() > 0) {
            this.deviationDirection = tokens[3].charAt(0);
        } else {
            this.deviationDirection = ' '; // 혹은 'N' 등 기본값
        }

        // Magnetic Variation
        this.magneticVariation = safeParseDouble(tokens[4], 0.0);

        // Variation Direction (E/W 등)
        if (tokens.length > 5 && tokens[5].length() > 0) {
            this.variationDirection = tokens[5].charAt(0);
        } else {
            this.variationDirection = ' ';
        }

        // 필요하다면 체크섬(*34) 검증 로직도 추가 가능
    }

    /**
     * 문자열 -> double 변환 시, 숫자 형식이 잘못되었을 경우를 대비해 예외 처리
     */
    private double safeParseDouble(String str, double defaultVal) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    // Getter
    public double getHeading() {
        return heading;
    }

    public double getMagneticDeviation() {
        return magneticDeviation;
    }

    public char getDeviationDirection() {
        return deviationDirection;
    }

    public double getMagneticVariation() {
        return magneticVariation;
    }

    public char getVariationDirection() {
        return variationDirection;
    }
}

