package com.gmt;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernDashboardUI extends JFrame {
    public ModernDashboardUI() {
        setTitle("Metronic Swing Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1️⃣ 사이드바 추가 (그라디언트 + 커스텀 메뉴 버튼)
        GradientPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 2️⃣ 상단 네비게이션 바 추가
        JPanel navbar = createNavbar();
        add(navbar, BorderLayout.NORTH);

        // 3️⃣ 메인 대시보드 패널 추가
        JPanel mainPanel = createMainDashboard();
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }


    /**
     * 📌 그라디언트 배경을 가진 패널
     */
    private static class GradientPanel extends JPanel {
        private final Color startColor;
        private final Color endColor;

        public GradientPanel(Color start, Color end) {
            this.startColor = start;
            this.endColor = end;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor); // 수직 그라디언트
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * 📌 사이드바 UI 생성 (그라디언트 + 메뉴 버튼)
     */
    private GradientPanel createSidebar() {
        GradientPanel sidebar = new GradientPanel(new Color(25, 25, 30), new Color(50, 50, 60));
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Metronic");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        sidebar.add(title, BorderLayout.NORTH);

        // 메뉴 버튼 추가
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1, 10, 10));
        menuPanel.setOpaque(false);

        menuPanel.add(createMenuButton("📊 Dashboards"));
        menuPanel.add(createMenuButton("🛍 eCommerce"));
        menuPanel.add(createMenuButton("📁 Projects"));
        menuPanel.add(createMenuButton("📈 Marketing"));
        menuPanel.add(createMenuButton("📌 Social"));
        menuPanel.add(createMenuButton("📖 Blog"));

        sidebar.add(menuPanel, BorderLayout.CENTER);
        return sidebar;
    }

    /**
     * 📌 상단 네비게이션 바 UI 생성
     */
    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navbar.setPreferredSize(new Dimension(getWidth(), 60));
        navbar.setBackground(Color.WHITE);
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));

        JButton addCustomerBtn = createStyledButton("➕ Add Customer");
        JButton newCampaignBtn = createStyledButton("🚀 New Campaign");

        JLabel profileIcon = new JLabel("👤");
        profileIcon.setFont(new Font("Arial", Font.PLAIN, 18));
        profileIcon.setBorder(new EmptyBorder(0, 10, 0, 10));

        navbar.add(addCustomerBtn);
        navbar.add(newCampaignBtn);
        navbar.add(profileIcon);

        return navbar;
    }

    /**
     * 📌 메인 대시보드 UI 생성
     */
    private JPanel createMainDashboard() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 카드 패널 (그라디언트)
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(1, 3, 10, 10));
        cardsPanel.setBackground(Color.WHITE);

        cardsPanel.add(createGradientCard("Avg. Client Rating", "7.8/10", new Color(50, 205, 50)));
        cardsPanel.add(createGradientCard("Instagram Followers", "730K", new Color(255, 69, 0)));
        cardsPanel.add(createGradientCard("Google Ads CPC", "$2.09", new Color(30, 144, 255)));

        mainPanel.add(cardsPanel, BorderLayout.NORTH);

        // 테이블 및 차트 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2, 10, 10));
        centerPanel.add(createTablePanel());
        centerPanel.add(createChartPanel());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * 📌 메뉴 버튼 생성 (JButton 스타일 개선)
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(40, 40, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 호버 효과 추가
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(70, 70, 90));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(40, 40, 50));
            }
        });

        return button;
    }

    /**
     * 📌 버튼 스타일링
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    /**
     * 📌 그라디언트 카드 UI 생성
     */
    private JPanel createGradientCard(String title, String value, Color gradientColor) {
        GradientPanel card = new GradientPanel(new Color(255, 255, 255), gradientColor);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 80));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.BLACK);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * 📌 테이블 UI 생성
     */
    private JScrollPane createTablePanel() {
        String[] columns = {"Campaign", "Platform", "Status", "Conversion"};
        Object[][] data = {
                {"Beats", "Email", "Sent", "18%"},
                {"Amazon", "Social Media", "Draft", "0.01%"},
                {"BP", "Google Ads", "Sent", "22%"},
                {"Slack", "Newsletter", "Scheduled", "15%"}
        };

        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        return new JScrollPane(table);
    }

    /**
     * 📌 차트 UI 생성
     */
    private JPanel createChartPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(55, "Social", "Jan");
        dataset.addValue(70, "Social", "Feb");
        dataset.addValue(90, "Social", "Mar");

        JFreeChart chart = ChartFactory.createBarChart("Marketing Performance", "Month", "Value", dataset);
        return new ChartPanel(chart);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ModernDashboardUI::new);
    }
}

