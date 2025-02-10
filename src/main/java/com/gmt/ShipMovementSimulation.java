package com.gmt;

import javax.swing.*;
import java.awt.*;

public class ShipMovementSimulation extends JPanel {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private double shipX = 100;  // 선박의 현재 X 좌표
    private double shipY = 300;  // 선박의 현재 Y 좌표
    private double speed = 5;    // 선박 속도 (1초마다 이동할 픽셀 수)
    private double direction = 0; // 이동 방향 (도 단위, 0도 = 오른쪽)

    public ShipMovementSimulation() {
        // Swing Timer를 사용하여 1초마다 선박 이동
        Timer timer = new Timer(1000, e -> moveShip());
        timer.start();
    }

    private void moveShip() {
        // 방향(도)을 라디안 값으로 변환하여 X, Y 이동 계산
        double angleRad = Math.toRadians(direction);
        shipX += Math.cos(angleRad) * speed;
        shipY -= Math.sin(angleRad) * speed; // Y 방향은 화면 좌표계에 따라 반대

        // 화면 다시 그리기
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // 선박 그리기 (원 형태)
        g.setColor(Color.GREEN);
        g.fillOval((int) shipX - 10, (int) shipY - 10, 20, 20);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ship Movement Simulation");
        ShipMovementSimulation panel = new ShipMovementSimulation();

        frame.add(panel);
        frame.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
