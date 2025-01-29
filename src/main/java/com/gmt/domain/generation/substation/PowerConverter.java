package com.gmt.domain.generation.substation;

/**
 * 전력변환장치(PowerConverter)
 */
public class PowerConverter {
    private String converterId;
    private double inputPower;  // 발전기로부터 입력받은 전력
    private double outputPower; // 변환 후 출력 전력

    public PowerConverter(String converterId, double inputPower, double outputPower) {
        this.converterId = converterId;
        this.inputPower = inputPower;
        this.outputPower = outputPower;
    }

    /**
     * 변환 효율 등을 고려하여 최종 출력 전력 계산
     */
    public void updateOutputPower() {
        double efficiency = 0.95; // 예시로 변환 효율 95%
        this.outputPower = inputPower * efficiency;
    }

    public void setInputPower(double inputPower) {
        this.inputPower = inputPower;
    }

    public double getOutputPower() {
        return outputPower;
    }

    @Override
    public String toString() {
        return String.format("PowerConverter[%s] inputPower=%.2f kW, outputPower=%.2f kW",
                converterId, inputPower, outputPower);
    }
}
