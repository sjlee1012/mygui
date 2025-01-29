package com.gmt.domain.generation.generator;

/**
 * 기어박스(GearBox)
 */
public class GearBox {
    private String gearBoxId;
    private double inputSpeed;      // 입력 회전속도(블레이드로부터 전달받는 값, rpm)
    private double outputSpeed;     // 출력 회전속도(rpm)
    private double gearRatio;       // 기어비
    private double torque;          // 출력 토크(Nm 단위 등 간단 가정)

    public GearBox(String gearBoxId, double inputSpeed, double outputSpeed, double gearRatio) {
        this.gearBoxId = gearBoxId;
        this.inputSpeed = inputSpeed;
        this.outputSpeed = outputSpeed;
        this.gearRatio = gearRatio;
    }

    public void updateOutputSpeed() {
        // 단순히 기어비를 곱해서 출력속도를 결정하는 예시
        this.outputSpeed = inputSpeed * gearRatio;
    }

    public double calculateTorque() {
        // 기어박스에서 단순히 기어비에 따른 토크 변환 예시
        // 실제 토크 계산은 블레이드나 풍속, 발전기 부하 등을 종합 고려해야 하지만 여기서는 간단히 가정
        this.torque = (inputSpeed > 0) ? (100 * gearRatio) : 0;
        return this.torque;
    }

    // getter, setter, toString ...
    public double getInputSpeed() {
        return inputSpeed;
    }

    public void setInputSpeed(double inputSpeed) {
        this.inputSpeed = inputSpeed;
    }

    public double getOutputSpeed() {
        return outputSpeed;
    }

    @Override
    public String toString() {
        return String.format("GearBox[%s] inputSpeed=%.2f rpm, outputSpeed=%.2f rpm, gearRatio=%.2f, torque=%.2f Nm",
                gearBoxId, inputSpeed, outputSpeed, gearRatio, torque);
    }
}
