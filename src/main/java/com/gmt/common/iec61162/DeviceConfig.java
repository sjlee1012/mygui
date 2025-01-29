package com.gmt.common.iec61162;

import java.util.List;

/**
 * devices.yaml 파일에서,
 *   - name: "DeviceA"
 *   - messages: ["GGA","RMC","GSA"]
 * 이런 구조를 매핑하는 POJO
 */
public class DeviceConfig {
    private String name;
    private List<String> messages;

    // 기본 생성자
    public DeviceConfig() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMessages() {
        return messages;
    }
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
