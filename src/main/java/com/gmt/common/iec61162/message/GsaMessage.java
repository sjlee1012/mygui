package com.gmt.common.iec61162.message;


import java.util.ArrayList;
import java.util.List;

public class GsaMessage extends NmeaMessage {

    private char mode1;    // 'M' or 'A'
    private int mode2;     // 1=No fix,2=2D,3=3D
    private final List<String> satellites = new ArrayList<>(); // 최대 12개 위성번호(PRN)
    private double pdop;
    private double hdop;
    private double vdop;

    public GsaMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $GPGSA,A,3,04,05,,,09,12,,,,,2.5,1.3,2.1*39
        String[] tokens = rawSentence.split(",");
        // tokens[0] = "$GPGSA"
        // tokens[1] = "A" (mode1)
        // tokens[2] = "3" (mode2)
        // tokens[3..14] = "04","05","",...,"12","",... (최대 12개)
        // tokens[15] = "2.5" (PDOP)
        // tokens[16] = "1.3" (HDOP)
        // tokens[17] = "2.1" (VDOP)
        // tokens[18] = "*39" (체크섬)

        this.mode1 = getCharToken(tokens, 1, 'A');
        this.mode2 = (int) safeParseDouble(tokens, 2, 1);

        // 위성 12개
        for (int i = 3; i <= 14; i++) {
            if (i < tokens.length && !tokens[i].isEmpty()) {
                satellites.add(tokens[i]);
            } else {
                satellites.add("");
            }
        }

        // PDOP, HDOP, VDOP
        this.pdop = safeParseDouble(tokens, 15, 0.0);
        this.hdop = safeParseDouble(tokens, 16, 0.0);
        this.vdop = safeParseDouble(tokens, 17, 0.0);
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

    // Getter
    public char getMode1() { return mode1; }
    public int getMode2() { return mode2; }
    public List<String> getSatellites() { return satellites; }
    public double getPdop() { return pdop; }
    public double getHdop() { return hdop; }
    public double getVdop() { return vdop; }
}

