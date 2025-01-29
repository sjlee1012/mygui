package com.gmt.common.iec61162.message;

public class MdaMessage extends NmeaMessage {

    // (1) 대표적인 MDA 필드들
    private double pressureInches;     // 기압(인치 단위)
    private double pressureBars;       // 기압(바 단위)
    private double airTemperature;     // 공기 온도(섭씨)
    private double waterTemperature;   // 수온(섭씨)
    private double relativeHumidity;   // 상대 습도(%)
    private double absoluteHumidity;   // 절대 습도(일반적으로 g/m³ 등, 실제 메시지마다 다름)
    private double dewPoint;           // 이슬점(섭씨)
    private double windDirectionTrue;  // 진북 기준 풍향(도 단위)
    private double windDirectionMag;   // 자북 기준 풍향(도 단위)
    private double windSpeedKnots;     // 풍속(노트)
    private double windSpeedMs;        // 풍속(m/s)

    public MdaMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $WIMDA,29.4474,I,0.9974,B,23.5,C,,,37.2,C,,,*12
        //               0       1 2    3    4    5 6 7  8    9 10 11 12 ...
        // 토큰을 ',' 기준으로 분리
        String[] tokens = rawSentence.split(",");

        // 인덱스(장치별/표준별로 다소 차이 있을 수 있음):
        // tokens[0] = "$WIMDA"
        // tokens[1] = barometric pressure (inches)
        // tokens[2] = "I"
        // tokens[3] = barometric pressure (bar)
        // tokens[4] = "B"
        // tokens[5] = air temp (C)
        // tokens[6] = "C"
        // tokens[7] = water temp (C)
        // tokens[8] = "C"
        // tokens[9] = relative humidity
        // tokens[10]= (빈 값 또는 추가 구분)
        // tokens[11]= absolute humidity
        // tokens[12]= ...
        // tokens[13]= dew point (C)
        // tokens[14]= "C"
        // tokens[15]= wind direction true (degrees)
        // tokens[16]= "T"
        // tokens[17]= wind direction mag (degrees)
        // tokens[18]= "M"
        // tokens[19]= wind speed (knots)
        // tokens[20]= "N"
        // tokens[21]= wind speed (m/s)
        // tokens[22]= "M*CS" (마지막에 체크섬 포함)

        // 안전한 숫자 파싱 (빈 문자열이나 형식 오류 대비)
        this.pressureInches     = safeParseDouble(tokens, 1);
        this.pressureBars       = safeParseDouble(tokens, 3);
        this.airTemperature     = safeParseDouble(tokens, 5);
        this.waterTemperature   = safeParseDouble(tokens, 7);
        this.relativeHumidity   = safeParseDouble(tokens, 9);
        this.absoluteHumidity   = safeParseDouble(tokens, 11);
        this.dewPoint           = safeParseDouble(tokens, 13);
        this.windDirectionTrue  = safeParseDouble(tokens, 15);
        this.windDirectionMag   = safeParseDouble(tokens, 17);
        this.windSpeedKnots     = safeParseDouble(tokens, 19);
        this.windSpeedMs        = safeParseDouble(tokens, 21);

        // 필요하다면 체크섬 검증 로직 추가
    }

    /**
     * 토큰 배열에서 특정 인덱스를 안전하게 double로 변환하는 헬퍼
     * 빈 문자열이나 NumberFormatException 발생 시 0.0 리턴 (예시)
     */
    private double safeParseDouble(String[] tokens, int index) {
        if (index >= tokens.length) {
            return 0.0;
        }
        String value = tokens[index];
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // (2) Getter
    public double getPressureInches() {
        return pressureInches;
    }
    public double getPressureBars() {
        return pressureBars;
    }
    public double getAirTemperature() {
        return airTemperature;
    }
    public double getWaterTemperature() {
        return waterTemperature;
    }
    public double getRelativeHumidity() {
        return relativeHumidity;
    }
    public double getAbsoluteHumidity() {
        return absoluteHumidity;
    }
    public double getDewPoint() {
        return dewPoint;
    }
    public double getWindDirectionTrue() {
        return windDirectionTrue;
    }
    public double getWindDirectionMag() {
        return windDirectionMag;
    }
    public double getWindSpeedKnots() {
        return windSpeedKnots;
    }
    public double getWindSpeedMs() {
        return windSpeedMs;
    }
}
