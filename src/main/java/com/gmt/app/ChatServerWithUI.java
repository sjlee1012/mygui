package com.gmt.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServerWithUI {

    private JFrame frame;
    private JTextArea logArea;
    private JButton startButton;
    private JButton stopButton;
    private JList<String> clientList;

    private ServerSocket serverSocket;
    private ExecutorService clientHandlerPool;
    private boolean isRunning = false;

    private final Map<String, PrintWriter> connectedClients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatServerWithUI::new);
    }

    public ChatServerWithUI() {
        setupUI();
    }

    private void setupUI() {
        frame = new JFrame("Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Top: Control Buttons
        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);

        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        frame.add(controlPanel, BorderLayout.NORTH);

        // Center: Log Area
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(logScrollPane, BorderLayout.CENTER);

        // Right: Client List
        clientList = new JList<>(new DefaultListModel<>());
        JScrollPane clientScrollPane = new JScrollPane(clientList);
        clientScrollPane.setBorder(BorderFactory.createTitledBorder("Connected Clients"));
        clientScrollPane.setPreferredSize(new Dimension(150, 0));
        frame.add(clientScrollPane, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(12345);
            clientHandlerPool = Executors.newCachedThreadPool();
            isRunning = true;

            log("Server started on port 12345");
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            new Thread(() -> {
                while (isRunning) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        log("New client connected: " + clientSocket.getInetAddress());
                        clientHandlerPool.submit(new ClientHandler(clientSocket));
                    } catch (IOException e) {
                        if (isRunning) log("Error accepting client connection: " + e.getMessage());
                    }
                }
            }).start();
        } catch (IOException e) {
            log("Error starting server: " + e.getMessage());
        }
    }

    private void stopServer() {
        try {
            isRunning = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (clientHandlerPool != null && !clientHandlerPool.isShutdown()) {
                clientHandlerPool.shutdownNow();
            }
            log("Server stopped");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);

            // Disconnect all clients
            connectedClients.values().forEach(writer -> writer.println("Server is shutting down..."));
            connectedClients.clear();
            updateClientList();
        } catch (IOException e) {
            log("Error stopping server: " + e.getMessage());
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    private void updateClientList() {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> model = (DefaultListModel<String>) clientList.getModel();
            model.clear();
            model.addAll(connectedClients.keySet());
        });
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Welcome to the chat server! Please enter your name:");
                clientName = in.readLine();
                if (clientName == null || clientName.isBlank()) {
                    socket.close();
                    return;
                }

                synchronized (connectedClients) {
                    connectedClients.put(clientName, out);
                }
                log(clientName + " joined the chat");
                updateClientList();

                String message;
                while ((message = in.readLine()) != null) {
                    log("[" + clientName + "]: " + message);
                    broadcast(clientName + ": " + message);
                }
            } catch (IOException e) {
                log("Connection error with client: " + clientName);
            } finally {
                disconnectClient();
            }
        }

        private void disconnectClient() {
            if (clientName != null) {
                log(clientName + " left the chat");
                synchronized (connectedClients) {
                    connectedClients.remove(clientName);
                }
                updateClientList();
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private void broadcast(String message) {
            synchronized (connectedClients) {
                connectedClients.values().forEach(writer -> writer.println(message));
            }
        }
    }
}
