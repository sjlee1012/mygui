package com.gmt.app;

import com.jcraft.jsch.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

public class FileTransferUI {

    private static Session session;
    private static ChannelSftp sftpChannel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("SSH File Transfer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        JPanel panel = new JPanel(new BorderLayout());

        // Top panel for connection settings
        //JPanel topPanel = new JPanel(new GridLayout(2, 4));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        topPanel.setBorder(BorderFactory.createTitledBorder("Connection Settings"));

        JLabel hostLabel = new JLabel("Host:");
        JTextField hostField = new JTextField("192.168.0.__", 15);

        JLabel portLabel = new JLabel("Port:");
        JTextField portField = new JTextField("22", 5);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField("parallels", 15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField("lee4173GF!", 15);

        topPanel.add(hostLabel);
        topPanel.add(hostField);
        topPanel.add(portLabel);
        topPanel.add(portField);
        topPanel.add(userLabel);
        topPanel.add(userField);
        topPanel.add(passLabel);
        topPanel.add(passField);

        // Table to display remote files
        DefaultTableModel remoteTableModel = new DefaultTableModel(new Object[]{"Name", "Size", "Modified"}, 0);
        JTable remoteTable = new JTable(remoteTableModel);
        JScrollPane remoteScrollPane = new JScrollPane(remoteTable);
        remoteScrollPane.setBorder(BorderFactory.createTitledBorder("Remote Files"));

        // Table to display local files
        DefaultTableModel localTableModel = new DefaultTableModel(new Object[]{"Name", "Size", "Modified"}, 0);
        JTable localTable = new JTable(localTableModel);
        JScrollPane localScrollPane = new JScrollPane(localTable);
        localScrollPane.setBorder(BorderFactory.createTitledBorder("Local Files"));

        // Split pane for local and remote file lists
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, localScrollPane, remoteScrollPane);
        splitPane.setDividerLocation(500);

        // Buttons for actions
        JPanel buttonPanel = new JPanel();
        JButton connectButton = new JButton("Connect");
        JButton downloadButton = new JButton("Download");
        JButton uploadButton = new JButton("Upload");

        buttonPanel.add(connectButton);
        buttonPanel.add(downloadButton);
        buttonPanel.add(uploadButton);

        // Add components to panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        // Populate local file list
        refreshLocalFiles(localTableModel);

        // Action Listeners
        connectButton.addActionListener(e -> {
            String host = hostField.getText();
            String user = userField.getText();
            String password = new String(passField.getPassword());
            int port;
            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                connectToServer(host, user, password, port);
                refreshRemoteFiles(remoteTableModel);
                JOptionPane.showMessageDialog(frame, "Connected to server successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Connection failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        downloadButton.addActionListener(e -> {
            int selectedRow = remoteTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a file to download.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String remoteFileName = remoteTableModel.getValueAt(selectedRow, 0).toString();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save File");

            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File localFile = fileChooser.getSelectedFile();
                try {
                    downloadFile(remoteFileName, localFile.getAbsolutePath());
                    JOptionPane.showMessageDialog(frame, "File downloaded successfully.");
                    refreshLocalFiles(localTableModel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Download failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        uploadButton.addActionListener(e -> {
            int selectedRow = localTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a file to upload.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String localFileName = localTableModel.getValueAt(selectedRow, 0).toString();
            String localFilePath = new File(localFileName).getAbsolutePath();
            String remotePath = JOptionPane.showInputDialog(frame, "Enter remote path:");

            if (remotePath != null && !remotePath.trim().isEmpty()) {
                try {
                    uploadFile(localFilePath, remotePath);
                    JOptionPane.showMessageDialog(frame, "File uploaded successfully.");
                    refreshRemoteFiles(remoteTableModel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Upload failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void connectToServer(String host, String user, String password, int port) throws Exception {
        JSch jsch = new JSch();
        session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftpChannel = (ChannelSftp) channel;
    }

    private static void refreshRemoteFiles(DefaultTableModel tableModel) throws Exception {
        tableModel.setRowCount(0);
        Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
        for (ChannelSftp.LsEntry entry : files) {
            tableModel.addRow(new Object[]{entry.getFilename(), entry.getAttrs().getSize(), entry.getAttrs().getMTime()});
        }
    }

    private static void refreshLocalFiles(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        File[] files = new File(System.getProperty("user.dir")).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    tableModel.addRow(new Object[]{file.getName(), file.length(), file.lastModified()});
                }
            }
        }
    }

    private static void downloadFile(String remoteFilePath, String localFilePath) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            sftpChannel.get(remoteFilePath, fos);
        }
    }

    private static void uploadFile(String localFilePath, String remoteFilePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(localFilePath)) {
            sftpChannel.put(fis, remoteFilePath);
        }
    }
}
