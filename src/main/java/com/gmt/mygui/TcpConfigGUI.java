package com.gmt.mygui;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class TcpConfigGUI {
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("TCP Configuration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // JPanel 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10)); // 여백 추가

        // 입력 패널 생성
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 2x2 그리드 레이아웃

        // IP 입력 구성
        JLabel ipLabel = new JLabel("IP Address:", SwingConstants.RIGHT);
        JTextField ipField = new JTextField(15);

        // Port 입력 구성
        JLabel portLabel = new JLabel("Port:", SwingConstants.RIGHT);
        JTextField portField = new JTextField(15);

        // 입력 패널에 컴포넌트 추가
        inputPanel.add(ipLabel);
        inputPanel.add(ipField);
        inputPanel.add(portLabel);
        inputPanel.add(portField);

        // Save 버튼 생성
        JButton saveButton = new JButton("Save to YAML");

        // 버튼 클릭 이벤트
        saveButton.addActionListener(e -> {
            String ip = ipField.getText();
            String port = portField.getText();

            if (validateIP(ip) && validatePort(port)) {
                Map<String, Object> config = new HashMap<>();
                config.put("ip_address", ip);
                config.put("port", Integer.parseInt(port));

                saveConfigToYaml(config);
                JOptionPane.showMessageDialog(frame, "Configuration saved to tcp_config.yaml!");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid IP or Port. Please check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 메인 패널에 컴포넌트 배치
        mainPanel.add(inputPanel, BorderLayout.CENTER); // 입력 필드 패널
        mainPanel.add(saveButton, BorderLayout.SOUTH);  // Save 버튼

        // JFrame에 메인 패널 추가
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    // IP 유효성 검사
    private static boolean validateIP(String ip) {
        String ipRegex = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ip.matches(ipRegex);
    }

    // Port 유효성 검사
    private static boolean validatePort(String port) {
        try {
            int portNum = Integer.parseInt(port);
            return portNum >= 1 && portNum <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // YAML 파일로 저장
    private static void saveConfigToYaml(Map<String, Object> config) {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);
        try (FileWriter writer = new FileWriter("tcp_config.yaml")) {
            yaml.dump(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save YAML file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
