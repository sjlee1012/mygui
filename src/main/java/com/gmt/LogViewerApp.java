package com.gmt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;

public class LogViewerApp extends JFrame {
    private JTabbedPane tabbedPane;
    private HashMap<String, LogPanel> logPanels = new HashMap<>(); // 파일 경로별 LogPanel 저장

    public LogViewerApp() {
        setTitle("BareTail - Java Swing Log Viewer");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 메뉴바 추가
        setJMenuBar(createMenuBar());

        // 탭 패널 추가
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * 메뉴바 생성
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open Log File");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(e -> chooseLogFile());

        JMenuItem closeTabItem = new JMenuItem("Close Current Tab");
        closeTabItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        closeTabItem.addActionListener(e -> closeCurrentTab());

        fileMenu.add(openItem);
        fileMenu.add(closeTabItem);
        menuBar.add(fileMenu);

        return menuBar;
    }

    /**
     * 사용자에게 파일 선택 창을 띄우고 로그 파일 추가
     */
    private void chooseLogFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Log File");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File logFile = fileChooser.getSelectedFile();
            addLogTab(logFile);
        }
    }

    /**
     * 로그 파일을 새 탭으로 추가
     */
    private void addLogTab(File logFile) {
        String filePath = logFile.getAbsolutePath();
        if (logPanels.containsKey(filePath)) {
            tabbedPane.setSelectedComponent(logPanels.get(filePath)); // 이미 존재하면 해당 탭으로 이동
            return;
        }

        LogPanel logPanel = new LogPanel(logFile);
        tabbedPane.addTab(logFile.getName(), logPanel);
        logPanels.put(filePath, logPanel);
        tabbedPane.setSelectedComponent(logPanel);
    }

    /**
     * 현재 활성화된 탭을 닫기
     */
    private void closeCurrentTab() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex != -1) {
            String filePath = tabbedPane.getTitleAt(selectedIndex);
            logPanels.remove(filePath);
            tabbedPane.remove(selectedIndex);
        }
    }

    /**
     * 로그 패널 클래스 (각 탭마다 독립적으로 동작)
     */
    private static class LogPanel extends JPanel {
        private JTable logTable;
        private DefaultTableModel tableModel;
        private Timer logUpdateTimer;
        private boolean autoScroll = true; // 자동 스크롤 여부
        private File logFile;

        public LogPanel(File logFile) {
            this.logFile = logFile;
            setLayout(new BorderLayout());

            // 테이블 설정
            String[] columnNames = {"Date", "Time", "Level", "Process ID", "Thread ID", "Class", "Method", "Message"};
            tableModel = new DefaultTableModel(columnNames, 0);
            logTable = new JTable(tableModel);
            logTable.setDefaultRenderer(Object.class, new LogCellRenderer());

            // 스크롤 패널 추가
            JScrollPane scrollPane = new JScrollPane(logTable);
            add(scrollPane, BorderLayout.CENTER);

            // 로그 업데이트 타이머 (1초마다 새 로그 추가)
            logUpdateTimer = new Timer();
            logUpdateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    loadLatestLogs();
                }
            }, 0, 1000);
        }

        /**
         * 최신 로그를 읽어서 테이블에 추가
         */
        private void loadLatestLogs() {
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = parseLogEntry(line);
                    if (parts != null) {
                        SwingUtilities.invokeLater(() -> {
                            tableModel.addRow(parts);
                            if (autoScroll) {
                                logTable.scrollRectToVisible(logTable.getCellRect(logTable.getRowCount() - 1, 0, true));
                            }
                        });
                    }
                }
            } catch (Exception e) {
                System.err.println("Error reading log file: " + e.getMessage());
            }
        }

        /**
         * 로그 파일 한 줄을 파싱하여 테이블 행 데이터로 변환
         */
        private String[] parseLogEntry(String logLine) {
            String[] parts = logLine.split("\\s+"); // 공백으로 분리
            if (parts.length < 8) return null;
            return new String[]{parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]};
        }
    }

    /**
     * 로그 레벨에 따라 행 색상 변경
     */
    static class LogCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String logLevel = (String) table.getValueAt(row, 2); // 로그 레벨 컬럼

            if (logLevel != null) {
                switch (logLevel) {
                    case "ERROR":
                        cell.setBackground(Color.RED);
                        cell.setForeground(Color.WHITE);
                        break;
                    case "WARN":
                        cell.setBackground(Color.YELLOW);
                        cell.setForeground(Color.BLACK);
                        break;
                    case "INFO":
                        cell.setBackground(Color.GREEN);
                        cell.setForeground(Color.BLACK);
                        break;
                    default:
                        cell.setBackground(Color.WHITE);
                        cell.setForeground(Color.BLACK);
                        break;
                }
            }
            return cell;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LogViewerApp::new);
    }
}
