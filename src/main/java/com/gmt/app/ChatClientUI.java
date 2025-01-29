package com.gmt.app;

import javax.swing.*;
import java.awt.*;

public class ChatClientUI {
    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("클라이언트");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // 메인 패널 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // 상단 패널 (서버 설정)
        JPanel serverPanel = new JPanel();
        serverPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel ipLabel = new JLabel("Server IP");
        JTextField ipField = new JTextField("192.168.0.__", 15);
        JLabel portLabel = new JLabel("Server Port");
        JTextField portField = new JTextField("5614", 5);
        JButton connectButton = new JButton("접속");
        JButton disconnectButton = new JButton("종료");

        serverPanel.add(ipLabel);
        serverPanel.add(ipField);
        serverPanel.add(portLabel);
        serverPanel.add(portField);
        serverPanel.add(connectButton);
        serverPanel.add(disconnectButton);

        // 중앙 패널 (받은 메시지)
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());

        JLabel receivedLabel = new JLabel("받은 메시지");
        JList<String> messageList = new JList<>(new String[] { "오션코딩학원입니다.", "어쩌라고요" });
        JScrollPane messageScrollPane = new JScrollPane(messageList);

        messagePanel.add(receivedLabel, BorderLayout.NORTH);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);

        // 하단 패널 (보낼 메시지)
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

        JLabel sendLabel = new JLabel("보낼 메시지");
        JTextField sendField = new JTextField();
        JPanel sendButtonPanel = new JPanel();
        sendButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton sendButton = new JButton("보내기");
        JButton clearButton = new JButton("채팅창 지움");

        sendButtonPanel.add(sendButton);
        sendButtonPanel.add(clearButton);

        sendPanel.add(sendLabel, BorderLayout.NORTH);
        sendPanel.add(sendField, BorderLayout.CENTER);
        sendPanel.add(sendButtonPanel, BorderLayout.SOUTH);

        // 메인 패널에 각 섹션 추가
        mainPanel.add(serverPanel, BorderLayout.NORTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(sendPanel, BorderLayout.SOUTH);

        // 프레임에 메인 패널 추가
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
