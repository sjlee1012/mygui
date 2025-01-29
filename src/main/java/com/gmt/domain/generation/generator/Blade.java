package com.gmt.domain.generation.generator;

/**
 * 블레이드(Blade)
 */
public class Blade {
    private String bladeId;
    private double length;         // 블레이드 길이 (m)
    private double rotationSpeed;  // 회전 속도 (rpm 등)

    public Blade(String bladeId, double length, double rotationSpeed) {
        this.bladeId = bladeId;
        this.length = length;
        this.rotationSpeed = rotationSpeed;
    }

    // getter, setter, toString ...
    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    @Override
    public String toString() {
        return String.format("Blade[%s] length=%.2f, rotationSpeed=%.2f rpm",
                bladeId, length, rotationSpeed);
    }
}
