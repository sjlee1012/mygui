package com.gmt.app;

import javax.swing.*;
import java.awt.*;

public class SimpleGUI {
    public static void main(String[] args) {
        // JFrame: 메인 윈도우
        JFrame frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200); // 창 크기 설정

        // JPanel: 컴포넌트 정렬을 위한 패널
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10)); // 여백 추가

        // 입력 필드와 레이블을 포함할 또 다른 패널
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // JLabel: 좌측 레이블
        JLabel label = new JLabel("Input:");
        inputPanel.add(label);

        // JTextField: 입력 필드
        JTextField textField = new JTextField(15); // 15 글자 길이의 텍스트 필드
        inputPanel.add(textField);

        // JButton: 버튼 생성
        JButton button = new JButton("Submit");

        // 버튼 클릭 이벤트 처리
        button.addActionListener(e -> {
            String inputText = textField.getText();
            if (!inputText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "You entered: " + inputText);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter something!");
            }
        });

        // 패널에 추가
        panel.add(inputPanel, BorderLayout.NORTH); // 입력 필드와 레이블
        panel.add(button, BorderLayout.SOUTH);     // 버튼

        // JFrame에 패널 추가
        frame.getContentPane().add(panel);

        // 화면에 표시
        frame.setVisible(true);
    }
}


