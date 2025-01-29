package com.gmt.common.iec61162.message;

import java.util.ArrayList;
import java.util.List;

public class GsvMessage extends NmeaMessage {

    private int totalMsgs;      // 전체 GSV 메시지 수
    private int msgNum;         // 현재 메시지 번호
    private int totalSats;      // 전체 위성 수
    private final List<SatelliteInfo> satellites = new ArrayList<>();

    public GsvMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $GPGSV,3,1,11,04,77,047,44,05,48,179,42,09,57,123,43,12,25,067,41*7E
        String[] tokens = rawSentence.split(",");
        // tokens[0] = "$GPGSV"
        // tokens[1] = "3" (전체 메시지 수)
        // tokens[2] = "1" (현재 메시지 번호)
        // tokens[3] = "11"(전체 위성 수)
        // 이후 4개의 위성 정보를 4토큰씩: PRN,Elev,Az,SNR
        // tokens[4..7]  -> 위성1
        // tokens[8..11] -> 위성2
        // tokens[12..15]-> 위성3
        // tokens[16..19]-> 위성4 (있다면)
        // ...
        this.totalMsgs = (int) safeParseDouble(tokens, 1, 1);
        this.msgNum = (int) safeParseDouble(tokens, 2, 1);
        this.totalSats = (int) safeParseDouble(tokens, 3, 0);

        int index = 4;
        while (index + 3 < tokens.length) {
            // 마지막 토큰에 체크섬(*)가 들어간 경우 처리
            // 예: tokens[7] = "44", tokens[19] = "41*7E"
            String prn = tokens[index];
            if (prn.contains("*")) break; // 체크섬 위치라면 종료

            double elev = safeParseDouble(tokens, index + 1, 0.0);
            double az = safeParseDouble(tokens, index + 2, 0.0);

            String snrStr = tokens[index + 3];
            double snr = 0.0;
            if (snrStr.contains("*")) {
                // 체크섬 잘라내기
                int starPos = snrStr.indexOf('*');
                if (starPos > 0) {
                    String clean = snrStr.substring(0, starPos);
                    snr = parseDoubleOrZero(clean);
                }
                satellites.add(new SatelliteInfo(prn, elev, az, snr));
                break;
            } else {
                snr = parseDoubleOrZero(snrStr);
                satellites.add(new SatelliteInfo(prn, elev, az, snr));
            }
            index += 4;
            if (index >= tokens.length) break;
        }
    }

    private double parseDoubleOrZero(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private double safeParseDouble(String[] tokens, int idx, double defaultVal) {
        if (idx >= tokens.length || tokens[idx].isEmpty()) return defaultVal;
        return parseDoubleOrZero(tokens[idx]);
    }

    public int getTotalMsgs() { return totalMsgs; }
    public int getMsgNum() { return msgNum; }
    public int getTotalSats() { return totalSats; }
    public List<SatelliteInfo> getSatellites() { return satellites; }

    // 내부 클래스 or 별도 파일로 관리 가능
    public static class SatelliteInfo {
        private final String prn;
        private final double elevation;
        private final double azimuth;
        private final double snr;

        public SatelliteInfo(String prn, double elevation, double azimuth, double snr) {
            this.prn = prn;
            this.elevation = elevation;
            this.azimuth = azimuth;
            this.snr = snr;
        }

        public String getPrn() { return prn; }
        public double getElevation() { return elevation; }
        public double getAzimuth() { return azimuth; }
        public double getSnr() { return snr; }
    }
}
