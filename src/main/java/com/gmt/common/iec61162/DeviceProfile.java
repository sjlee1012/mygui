package com.gmt.common.iec61162;

import java.util.Set;

/**
 * 특정 장치(Device)가 지원하는 NMEA 메시지 타입 목록 등을 담는 프로필
 */
public class DeviceProfile {

    private final String deviceName;             // 장치명/ID
    private final Set<String> supportedMessages; // 지원하는 메시지 타입 (GGA,RMC 등)

    public DeviceProfile(String deviceName, Set<String> supportedMessages) {
        this.deviceName = deviceName;
        this.supportedMessages = supportedMessages;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public boolean isMessageSupported(String messageType) {
        return supportedMessages.contains(messageType);
    }

    @Override
    public String toString() {
        return "DeviceProfile{" +
                "deviceName='" + deviceName + '\'' +
                ", supportedMessages=" + supportedMessages +
                '}';
    }
}
