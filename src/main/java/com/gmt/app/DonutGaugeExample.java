package com.gmt.app;

import javax.swing.*;
import java.awt.*;

public class DonutGaugeExample extends JFrame {

    public DonutGaugeExample() {
        setTitle("Donut Gauge Example");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 게이지에 표시할 값(0~60 사이)
        // 예: margin = 41.3
        DonutGaugePanel gaugePanel = new DonutGaugePanel(41.3);
        add(gaugePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DonutGaugeExample().setVisible(true);
        });
    }
}

class DonutGaugePanel extends JPanel {

    private double margin; // 0~60 범위 값

    public DonutGaugePanel(double margin) {
        this.margin = margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 2D 활성화
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int centerX = w / 2;
        int centerY = h / 2;

        // 바깥쪽 게이지의 반경
        int outerRadius = Math.min(w, h) / 2 - 20;

        // 게이지 그릴 범위(원의 bounding box)
        int arcX = centerX - outerRadius;
        int arcY = centerY - outerRadius;
        int arcW = outerRadius * 2;
        int arcH = outerRadius * 2;

        // (1) 배경 게이지: -120° ~ +120° 범위(총 240°)
        //     0 ~ 60까지 60단계로 나누어 조금씩 fillArc
        double totalRange = 60.0;
        double angleRange = 240.0;  // -120°~+120° = 240°
        double angleStep = angleRange / totalRange; // 4°

        for (int i = 0; i < 60; i++) {
            double startAngle = -120 + i * angleStep;
            int arcAngle = (int) Math.ceil(angleStep);

            // 그라디언트 진행도(0~1)
            double t = i / totalRange;
            Color c = interpolateColor(t); // 빨강→노랑→초록 보간
            g2.setColor(c);

            g2.fillArc(arcX, arcY, arcW, arcH, (int) Math.round(startAngle), arcAngle);
        }


        // (3) 바늘(포인터) - 필요 없으면 이 부분 제거
        double pointerAngle = -120 + (margin / totalRange) * angleRange;
        double radian = Math.toRadians(pointerAngle);

        int needleLength = (int) (outerRadius * 0.9);
        int needleX = centerX + (int) (needleLength * Math.cos(radian));
        int needleY = centerY + (int) (needleLength * Math.sin(radian));

        float lineThickness = 8.0f;
        g2.setStroke(new BasicStroke(lineThickness));
        g2.setColor(Color.BLACK);
        g2.drawLine(centerX, centerY, needleX, needleY);

        // (2) 가운데 구멍(둥근 여백)을 흰색으로 채워 도넛 형태 만들기
        //     외경(outerRadius)의 절반 정도를 예시로 지정
        int innerRadius = outerRadius / 2;
        g2.setColor(Color.WHITE);
        g2.fillOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);


        // (4) 도넛 중앙 라벨 + 값 표시
        //     예: "Margin" (굵게), "41.3%" (보통)
        String label = "Margin";
        String valueText = String.format("%.1f%%", margin);

        // 라벨 폰트
        Font labelFont = getFont().deriveFont(Font.BOLD, 18f);
        g2.setFont(labelFont);
        FontMetrics fmLabel = g2.getFontMetrics();
        int labelWidth = fmLabel.stringWidth(label);
        // 라벨을 수직 중앙에 맞추려면, 기준선을 조금 위로 잡아야 됨
        int labelX = centerX - labelWidth / 2;
        int labelY = centerY - 5; // 살짝 위로

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(label, labelX, labelY);

        // 값 폰트
        Font valueFont = getFont().deriveFont(Font.PLAIN, 16f);
        g2.setFont(valueFont);
        FontMetrics fmValue = g2.getFontMetrics();
        int valueWidth = fmValue.stringWidth(valueText);
        int valueX = centerX - valueWidth / 2;
        // 라벨 바로 아래에 표시. 라벨 y값을 기준으로 + fmValue.getAscent()
        int valueY = labelY + fmValue.getAscent() + 5;
        g2.drawString(valueText, valueX, valueY);

        g2.dispose();
    }

    /**
     * 0.0 ≤ t ≤ 1.0 구간에서
     * 빨강(0%) → 노랑(50%) → 초록(100%) 그라디언트 색상 반환
     */
    private Color interpolateColor(double t) {
        // t ≤ 0.5: 빨강(255,0,0) → 노랑(255,255,0)
        // t > 0.5: 노랑(255,255,0) → 초록(0,255,0)
        if (t <= 0.5) {
            double ratio = t / 0.5; // 0 ~ 1
            int r = 255;
            int g = (int) (0 + (255 - 0) * ratio);
            int b = 0;
            return new Color(r, g, b);
        } else {
            double ratio = (t - 0.5) / 0.5; // 0 ~ 1
            int r = (int) (255 + (0 - 255) * ratio);
            int g = 255;
            int b = 0;
            return new Color(r, g, b);
        }
    }
}
