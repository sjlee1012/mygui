package com.gmt.common.iec61162.message;

public abstract class NmeaMessage {
    protected final String rawSentence;  // 원본 NMEA 문자열
    protected final String talker;       // 예: "GP"
    protected final String messageType;  // 예: "GGA"

    protected NmeaMessage(String rawSentence) {
        this.rawSentence = rawSentence;

        // 최소한의 공통 처리
        this.talker = extractTalker(rawSentence);
        this.messageType = extractMessageType(rawSentence);

        // 체크섬 검증 등 공통 로직이 필요하면 추가
        // if (!validateChecksum(rawSentence)) {
        //     throw new IllegalArgumentException("Invalid checksum: " + rawSentence);
        // }
    }

    // 하위 클래스에서 구현해야 할 필드 파싱 로직
    public abstract void parseFields();

    // 간단히 "$GPGGA" → "GP"
    protected String extractTalker(String sentence) {
        // 예: "$GPGGA" → "GP"
        return sentence.substring(1, 3);
    }
    protected String extractMessageType(String sentence) {
        // 예: "$GPGGA" → "GGA"
        return sentence.substring(3, 6);
    }

    public String getRawSentence() {
        return rawSentence;
    }
    public String getTalker() {
        return talker;
    }
    public String getMessageType() {
        return messageType;
    }
}
