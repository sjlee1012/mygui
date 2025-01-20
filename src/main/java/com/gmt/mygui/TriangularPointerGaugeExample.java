package com.gmt.mygui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class TriangularPointerGaugeExample extends JFrame {

    public TriangularPointerGaugeExample() {
        setTitle("Triangular Pointer Gauge Example");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 예: margin = 41.3 (0~60 범위)
        TriangularPointerGaugePanel gaugePanel = new TriangularPointerGaugePanel(41.3);
        add(gaugePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TriangularPointerGaugeExample().setVisible(true);
        });
    }
}

class TriangularPointerGaugePanel extends JPanel {

    private double margin; // 0~60 범위 값

    public TriangularPointerGaugePanel(double margin) {
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

        // (1) 도넛(바깥/안쪽) 반경 설정
        int outerRadius = Math.min(w, h) / 2 - 20; // 바깥쪽
        int innerRadius = outerRadius / 2;         // 안쪽

        // 게이지 호(arc) 그릴 때 사용
        int arcX = centerX - outerRadius;
        int arcY = centerY - outerRadius;
        int arcW = outerRadius * 2;
        int arcH = outerRadius * 2;

        // (2) 배경 게이지(그라디언트)
        double totalRange = 60.0;     // 값 범위
        double angleRange = 240.0;    // -120° ~ +120°
        double angleStep  = angleRange / totalRange; // 4°

        // 빨강→노랑→초록 그라디언트로 60개 호(arc) 채우기
        for (int i = 0; i < 60; i++) {
            double startAngle = -120 + i * angleStep;
            int arcAngle = (int) Math.ceil(angleStep);

            double t = i / totalRange;  // 0 ~ 1
            Color c = interpolateColor(t);
            g2.setColor(c);
            g2.fillArc(arcX, arcY, arcW, arcH, (int) Math.round(startAngle), arcAngle);
        }

        // (3) 도넛 중앙 흰색
        g2.setColor(Color.WHITE);
        g2.fillOval(centerX - innerRadius, centerY - innerRadius,
                innerRadius * 2, innerRadius * 2);

        // (2) 삼각형 지침
        // 삼각형 끝점과 밑변 정의
        double apexLength = outerRadius * 0.7;  // 삼각형 끝
        double baseLength = outerRadius * 0.4; // 삼각형 밑변 위치
        double halfBaseWidth = 10;             // 삼각형 밑변 폭 절반

        // 삼각형 경로 정의
        Path2D pointer = new Path2D.Double();
        pointer.moveTo(apexLength, 0);                       // 끝점 (삼각형 꼭짓점)
        pointer.lineTo(baseLength, -halfBaseWidth);          // 밑변 왼쪽
        pointer.lineTo(baseLength, +halfBaseWidth);          // 밑변 오른쪽
        pointer.closePath();                                 // 삼각형 닫기

        // 회전 변환 (예: 90도 = 정중앙)
        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY); // 중심으로 이동
        transform.rotate(Math.toRadians(45));  // 90도 회전
        Shape rotatedPointer = transform.createTransformedShape(pointer);

        // 삼각형 지침 그리기
        g2.setColor(Color.BLACK);
        g2.fill(rotatedPointer);


        // (5) 중앙 라벨/값 표시
        String label = "Margin";
        String valueText = String.format("%.1f%%", margin);

        // 라벨
        Font labelFont = getFont().deriveFont(Font.BOLD, 18f);
        g2.setFont(labelFont);
        FontMetrics fmLabel = g2.getFontMetrics();
        int labelWidth = fmLabel.stringWidth(label);
        int labelX = centerX - labelWidth / 2;
        int labelY = centerY - 5; // 조금 위로
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(label, labelX, labelY);

        // 값
        Font valueFont = getFont().deriveFont(Font.PLAIN, 16f);
        g2.setFont(valueFont);
        FontMetrics fmValue = g2.getFontMetrics();
        int valueWidth = fmValue.stringWidth(valueText);
        int valueX = centerX - valueWidth / 2;
        int valueY = labelY + fmValue.getAscent() + 5;
        g2.drawString(valueText, valueX, valueY);

        g2.dispose();
    }

    /**
     * (0 <= t <= 1)에서
     * 빨강(0%) → 노랑(50%) → 초록(100%)으로 선형 보간
     */
    private Color interpolateColor(double t) {
        // t <= 0.5 : 빨강(255,0,0) -> 노랑(255,255,0)
        // t > 0.5  : 노랑(255,255,0) -> 초록(0,255,0)
        if (t <= 0.5) {
            double ratio = t / 0.5;
            int r = 255;
            int g = (int)(0 + (255 - 0) * ratio);
            int b = 0;
            return new Color(r, g, b);
        } else {
            double ratio = (t - 0.5) / 0.5;
            int r = (int)(255 + (0 - 255) * ratio);
            int g = 255;
            int b = 0;
            return new Color(r, g, b);
        }
    }
}
