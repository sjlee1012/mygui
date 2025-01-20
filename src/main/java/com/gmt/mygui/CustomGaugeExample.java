package com.gmt.mygui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class CustomGaugeExample extends JFrame {

    public CustomGaugeExample() {
        setTitle("Custom Gauge Example");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CustomGaugePanel gaugePanel = new CustomGaugePanel();
        add(gaugePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomGaugeExample().setVisible(true);
        });
    }
}

class CustomGaugePanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int centerX = w / 2;
        int centerY = h / 2;

        // (1) 도넛 배경
        int outerRadius = Math.min(w, h) / 2 - 20;
        int innerRadius = outerRadius / 2;

        // 바깥 원(검은색)
        g2.setColor(Color.GRAY);
        g2.fillOval(centerX - outerRadius, centerY - outerRadius, outerRadius * 2, outerRadius * 2);
        //g2.fillOval(centerX - outerRadius, centerY - outerRadius, outerRadius * 2, outerRadius * 2);

        // 중앙의 흰색 원
        g2.setColor(Color.WHITE);
        g2.fillOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);

        // (2) 삼각형 지침
        // 삼각형 끝점과 밑변 정의
        double apexLength = outerRadius * 0.9;  // 삼각형 끝
        double baseLength = outerRadius * 0.6; // 삼각형 밑변 위치
        double halfBaseWidth = 20;             // 삼각형 밑변 폭 절반

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

        // (3) 중앙 고정 핀(원형 디스크)
        int pinRadius = innerRadius / 5; // 고정 핀 크기
        g2.setColor(Color.BLACK);
        g2.fillOval(centerX - pinRadius, centerY - pinRadius, pinRadius * 2, pinRadius * 2);

        g2.dispose();
    }
}
