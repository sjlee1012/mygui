package com.gmt.app;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatClientWithUI {

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton connectButton;
    private JTextField serverField;
    private JTextField portField;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private boolean connected = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClientWithUI::new);
    }

    public ChatClientWithUI() {
        setupUI();
    }

    private void setupUI() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Top: Connection Panel
        JPanel connectionPanel = new JPanel(new FlowLayout());
        serverField = new JTextField("localhost", 15);
        portField = new JTextField("12345", 5);
        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> connectToServer());

        connectionPanel.add(new JLabel("Server:"));
        connectionPanel.add(serverField);
        connectionPanel.add(new JLabel("Port:"));
        connectionPanel.add(portField);
        connectionPanel.add(connectButton);

        frame.add(connectionPanel, BorderLayout.NORTH);

        // Center: Chat Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        frame.add(chatScrollPane, BorderLayout.CENTER);

        // Bottom: Message Panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        frame.add(messagePanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void connectToServer() {
        if (connected) {
            disconnectFromServer();
            return;
        }

        String serverAddress = serverField.getText().trim();
        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid port number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            connected = true;
            connectButton.setText("Disconnect");
            sendButton.setEnabled(true);
            messageField.setEnabled(true);
            log("Connected to server " + serverAddress + ":" + port);

            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void disconnectFromServer() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            log("Error while disconnecting: " + e.getMessage());
        } finally {
            connected = false;
            connectButton.setText("Connect");
            sendButton.setEnabled(false);
            messageField.setEnabled(false);
            log("Disconnected from server.");
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && connected) {
            out.println(message);
            messageField.setText("");
        }
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                log("Server: " + message);
            }
        } catch (IOException e) {
            log("Connection lost: " + e.getMessage());
        } finally {
            disconnectFromServer();
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
}

