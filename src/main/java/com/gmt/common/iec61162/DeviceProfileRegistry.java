package com.gmt.common.iec61162;

import java.util.HashMap;
import java.util.Map;

/**
 * 여러 DeviceProfile을 등록/조회하는 전역 레지스트리
 */
public class DeviceProfileRegistry {

    private static final Map<String, DeviceProfile> PROFILE_MAP = new HashMap<>();

    public static void registerProfile(String deviceName, DeviceProfile profile) {
        PROFILE_MAP.put(deviceName, profile);
    }

    public static DeviceProfile getProfile(String deviceName) {
        return PROFILE_MAP.get(deviceName);
    }
}
