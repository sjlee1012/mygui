package com.gmt.domain.generation.generator;

/**
 * 터빈발전기(Generator)
 */
public class Generator {
    private String generatorId;
    private double inputTorque;          // 기어박스에서 전달받은 토크
    private double currentPowerOutput;   // 현재 출력 전력 (kW 등)

    public Generator(String generatorId, double inputTorque, double currentPowerOutput) {
        this.generatorId = generatorId;
        this.inputTorque = inputTorque;
        this.currentPowerOutput = currentPowerOutput;
    }

    /**
     * 간단한 전력 계산 예시
     */
    public void updatePowerOutput() {
        // 여기서는 토크가 어느 정도 이상이면 일정 전력 출력한다고 단순 가정
        if (inputTorque > 0) {
            this.currentPowerOutput = inputTorque * 0.8; // 예: 토크 * 0.8 = kW
        } else {
            this.currentPowerOutput = 0.0;
        }
    }

    // getter, setter, toString ...
    public double getCurrentPowerOutput() {
        return currentPowerOutput;
    }

    public void setInputTorque(double inputTorque) {
        this.inputTorque = inputTorque;
    }

    @Override
    public String toString() {
        return String.format("Generator[%s] inputTorque=%.2f Nm, currentPowerOutput=%.2f kW",
                generatorId, inputTorque, currentPowerOutput);
    }
}
