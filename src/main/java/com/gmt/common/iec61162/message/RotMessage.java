package com.gmt.common.iec61162.message;

public class RotMessage extends NmeaMessage {

    private double rateOfTurn; // 도/분
    private char status;       // 'A' (유효), 혹은 다른 문자가 올 수도 있음

    public RotMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $HEROT,2.5,A*3F
        String[] tokens = rawSentence.split(",");

        // tokens[0] = "$HEROT"
        // tokens[1] = "2.5"   (rateOfTurn)
        // tokens[2] = "A"     (status) -> A=valid
        this.rateOfTurn = safeParseDouble(tokens, 1, 0.0);

        if (tokens.length > 2 && !tokens[2].isEmpty()) {
            this.status = tokens[2].charAt(0);
        } else {
            this.status = ' ';
        }
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
    public double getRateOfTurn() {
        return rateOfTurn;
    }
    public char getStatus() {
        return status;
    }
}
