package com.gmt.common.iec61162.message;

import java.util.ArrayList;
import java.util.List;

public class XdrMessage extends NmeaMessage {

    private final List<XdrData> xdrDataList = new ArrayList<>();

    public XdrMessage(String rawSentence) {
        super(rawSentence);
        parseFields();
    }

    @Override
    public void parseFields() {
        // 예: $HCXDR,A,18.0,D,PTCH,A,2.0,D,ROLL*4D
        // tokens[0] = "$HCXDR"
        // 이후 4개씩 하나의 세트
        String[] tokens = rawSentence.split(",");

        // tokens[1] = "A"   (Type1)
        // tokens[2] = "18.0"(Data1)
        // tokens[3] = "D"   (Unit1)
        // tokens[4] = "PTCH"(ID1)
        // tokens[5] = "A"   (Type2)
        // tokens[6] = "2.0" (Data2)
        // tokens[7] = "D"   (Unit2)
        // tokens[8] = "ROLL"(ID2)
        // tokens[9] = "*4D" (체크섬)

        // 세트는 4개 토큰씩, 첫 토큰($HCXDR)은 건너뛰고,
        // 마지막 체크섬 토큰은 예외적으로 처리해야 할 수 있음
        // 안전하게, (length - 1)까지만 반복하고, or 체크섬 전까지

        int index = 1; // 0번은 "$HCXDR"
        while (index + 3 < tokens.length) {
            // type
            String type = tokens[index];
            // data
            double data = safeParseDouble(tokens[index + 1], 0.0);
            // unit
            String unit = tokens[index + 2];
            // id
            String id = tokens[index + 3];

            // 만약 마지막 token이 "*4D" 형태이면, 별표 전까지 잘라내야 할 수도 있음
            // 그러나 일반적으로 *CheckSum 은 맨 마지막 한 토큰에만 들어가므로
            // 여기서는 단순하게 if-check
            if (id.contains("*")) {
                // 체크섬 토큰
                int starPos = id.indexOf('*');
                if (starPos > 0) {
                    id = id.substring(0, starPos);
                }
                // 파싱 끝
                xdrDataList.add(new XdrData(type, data, unit, id));
                break;
            } else {
                xdrDataList.add(new XdrData(type, data, unit, id));
            }

            index += 4; // 다음 세트로 이동
        }
    }

    private double safeParseDouble(String str, double defaultVal) {
        if (str == null || str.isEmpty()) return defaultVal;
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public List<XdrData> getXdrDataList() {
        return xdrDataList;
    }
}
