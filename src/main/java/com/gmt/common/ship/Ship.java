package com.gmt.common.ship;


/**
 * 선박 객체
 */
public class Ship {
    private String shipId;
    private double x; // 현재 x 좌표
    private double y; // 현재 y 좌표

    // 이전 위치(진입/이탈 이벤트 확인용)
    private double prevX;
    private double prevY;

    public Ship(String shipId, double x, double y) {
        this.shipId = shipId;
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
    }

    public String getShipId() {
        return shipId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getPrevX() {
        return prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public void move(double newX, double newY) {
        this.prevX = this.x;
        this.prevY = this.y;
        this.x = newX;
        this.y = newY;
    }

    @Override
    public String toString() {
        return String.format("Ship[%s] current=(%.4f, %.4f)", shipId, x, y);
    }
}

