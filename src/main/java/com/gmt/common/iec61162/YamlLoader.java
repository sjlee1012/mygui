package com.gmt.common.iec61162;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * SnakeYAML을 이용해 /devices.yaml 리소스를 읽어
 * DevicesYaml 객체로 파싱하는 로더 예시
 */
public class YamlLoader {

    public static DevicesYaml loadDevicesYaml(String yamlResourcePath) {
        Yaml yaml = new Yaml();
        try (InputStream is = YamlLoader.class.getResourceAsStream(yamlResourcePath)) {
            if (is == null) {
                throw new IllegalArgumentException("YAML resource not found: " + yamlResourcePath);
            }
            return yaml.loadAs(is, DevicesYaml.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML: " + yamlResourcePath, e);
        }
    }
}

