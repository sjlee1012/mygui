package com.gmt.common.iec61162.message;

public class ZdaMessage extends NmeaMessage {

    private String utcTime;    // hhmmss.ss
    private int day;           // dd
    private int month;         // mm
    private int year;          // yyyy
    private int localHour;     // xx
    private int localMinute;   // yy

    public ZdaMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $GPZDA,201530.00,04,07,2002,00,00*60
        String[] tokens = rawSentence.split(",");
        // tokens[0] = "$GPZDA"
        // tokens[1] = "201530.00" (UTC)
        // tokens[2] = "04" (일)
        // tokens[3] = "07" (월)
        // tokens[4] = "2002" (연도)
        // tokens[5] = "00" (지역시-시)
        // tokens[6] = "00" (지역시-분)
        // tokens[7] = "*60" (체크섬)

        this.utcTime = getStringToken(tokens, 1);
        this.day = (int) safeParseDouble(tokens, 2, 0.0);
        this.month = (int) safeParseDouble(tokens, 3, 0.0);
        this.year = (int) safeParseDouble(tokens, 4, 0.0);
        this.localHour = (int) safeParseDouble(tokens, 5, 0.0);
        this.localMinute = (int) safeParseDouble(tokens, 6, 0.0);
    }

    private double safeParseDouble(String[] tokens, int idx, double def) {
        if (idx >= tokens.length || tokens[idx].isEmpty()) return def;
        try {
            return Double.parseDouble(tokens[idx]);
        } catch (NumberFormatException e) {
            return def;
        }
    }
    private String getStringToken(String[] tokens, int idx) {
        if (idx < tokens.length) return tokens[idx];
        return "";
    }

    // Getter
    public String getUtcTime() { return utcTime; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public int getLocalHour() { return localHour; }
    public int getLocalMinute() { return localMinute; }
}
