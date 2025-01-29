package com.gmt.common.iec61162;

import java.util.List;

/**
 * devices.yaml 파일 전체 구조:
 * devices:
 *   - { name: ..., messages: [...] }
 *   - ...
 */
public class DevicesYaml {
    private List<DeviceConfig> devices;

    public DevicesYaml() {}

    public List<DeviceConfig> getDevices() {
        return devices;
    }
    public void setDevices(List<DeviceConfig> devices) {
        this.devices = devices;
    }
}

