package com.gmt.common.iec61162;

import com.gmt.common.iec61162.message.NmeaMessage;

public class NmeaMessageFactory {

    /**
     * 특정 장치(deviceName)에서 들어온 rawSentence를 파싱.
     * 1) 장치 프로필에서 메시지 지원 여부 확인
     * 2) 메시지 레지스트리에서 해당 타입의 클래스 찾아 Reflection으로 인스턴스 생성
     */
    public static NmeaMessage create(String rawSentence, String deviceName) {
        // 1) 장치 프로필 조회
        DeviceProfile profile = DeviceProfileRegistry.getProfile(deviceName);
        if (profile == null) {
            throw new IllegalArgumentException("Unknown device: " + deviceName);
        }

        // 2) 메시지 타입 추출 (예: "$GPGGA" → "GGA")
        String msgType = extractMessageType(rawSentence);

        // 3) 장치에서 해당 메시지 지원 여부 체크
        if (!profile.isMessageSupported(msgType)) {
            throw new UnsupportedOperationException(
                    "Device [" + deviceName + "] does not support messageType [" + msgType + "]"
            );
        }

        // 4) 메시지 레지스트리에서 클래스 가져와 Reflection 생성
        Class<? extends NmeaMessage> clazz = NmeaMessageRegistry.getMessageClass(msgType);
        if (clazz == null) {
            throw new UnsupportedOperationException(
                    "No registered class for messageType [" + msgType + "]"
            );
        }

        try {
            return clazz.getConstructor(String.class).newInstance(rawSentence);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate message class: " + clazz.getName(), e);
        }
    }

    private static String extractMessageType(String sentence) {
        // 단순 예: "$GPGGA..." → "GGA"
        // 실제 구현은 안전성(인덱스 체크 등) 보완
        return sentence.substring(3, 6);
    }
}
