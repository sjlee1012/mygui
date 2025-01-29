package com.gmt.common.iec61162;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DevicesYaml → DeviceProfile을 만들어 DeviceProfileRegistry에 등록
 */
public class DeviceProfileLoader {

    public static void loadProfilesFromYaml(String yamlPath) {
        DevicesYaml devicesYaml = YamlLoader.loadDevicesYaml(yamlPath);
        if (devicesYaml == null || devicesYaml.getDevices() == null) {
            return;
        }

        List<DeviceConfig> configs = devicesYaml.getDevices();
        for (DeviceConfig cfg : configs) {
            String name = cfg.getName();
            Set<String> msgSet = new HashSet<>(cfg.getMessages());

            DeviceProfile profile = new DeviceProfile(name, msgSet);
            DeviceProfileRegistry.registerProfile(name, profile);
        }
    }
}
