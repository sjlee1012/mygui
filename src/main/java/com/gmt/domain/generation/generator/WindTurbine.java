package com.gmt.domain.generation.generator;

import com.gmt.domain.generation.substation.PowerConverter;

/**
 * 풍력발전기(WindTurbine) - 블레이드, 기어박스, 터빈발전기, 전력변환장치를 포함
 */
public class WindTurbine {
    private String id;
    private Blade[] blades;
    private GearBox gearBox;
    private Generator generator;
    private PowerConverter powerConverter;

    // 예시로 간단히 추가한 필드
    private double totalPowerOutput;    // 발전기 + 전력변환장치를 거쳐 최종 출력되는 전력(kW 단위 등)

    public WindTurbine(String id, Blade[] blades, GearBox gearBox, Generator generator, PowerConverter powerConverter) {
        this.id = id;
        this.blades = blades;
        this.gearBox = gearBox;
        this.generator = generator;
        this.powerConverter = powerConverter;
    }

    /**
     * 풍속에 따른 전체 컴포넌트 상태 갱신 로직 (간단 예시)
     *
     * @param windSpeed 현재 풍속 (m/s)
     */
    public void updateTurbineState(double windSpeed) {
        // 1) 블레이드 회전속도 증가 (단순 비례 계산 예시)
        for (Blade blade : blades) {
            double newSpeed = windSpeed * 10; // 풍속 1 m/s 당 회전속도 10rpm 증가 (예시)
            blade.setRotationSpeed(newSpeed);
        }

        // 2) 기어박스 출력속도 업데이트
        //    블레이드 회전속도를 종합한 평균값을 기어박스 입력속도로 사용 (매우 단순화된 예시)
        double averageBladeSpeed = getAverageBladeSpeed();
        gearBox.setInputSpeed(averageBladeSpeed);
        gearBox.updateOutputSpeed();

        // 3) 터빈발전기(Generator) 출력 전력 계산
        generator.setInputTorque(gearBox.calculateTorque());  // 기어박스 출력토크를 전달 (예시)
        generator.updatePowerOutput();

        // 4) 전력변환장치(PowerConverter)를 통해 최종 출력 전력 결정
        powerConverter.setInputPower(generator.getCurrentPowerOutput());
        powerConverter.updateOutputPower();

        // 5) 풍력발전기의 전체 출력 전력 계산
        this.totalPowerOutput = powerConverter.getOutputPower();
    }

    /**
     * 블레이드 멈춤(비상 정지 등)
     */
    public void stopBlades() {
        for (Blade blade : blades) {
            blade.setRotationSpeed(0.0);
        }
        // 블레이드가 멈추면 기어박스, 발전기도 사실상 0 으로
        gearBox.setInputSpeed(0.0);
        gearBox.updateOutputSpeed();

        generator.setInputTorque(0.0);
        generator.updatePowerOutput();

        powerConverter.setInputPower(0.0);
        powerConverter.updateOutputPower();

        this.totalPowerOutput = 0.0;
    }

    private double getAverageBladeSpeed() {
        double sum = 0.0;
        for (Blade blade : blades) {
            sum += blade.getRotationSpeed();
        }
        return sum / blades.length;
    }

    // getter, setter, toString ...

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WindTurbine ID: ").append(id).append("\n");
        for (Blade blade : blades) {
            sb.append("  ").append(blade).append("\n");
        }
        sb.append("  ").append(gearBox).append("\n");
        sb.append("  ").append(generator).append("\n");
        sb.append("  ").append(powerConverter).append("\n");
        sb.append("  Total Power Output: ").append(totalPowerOutput).append(" kW (예시 단위)\n");
        return sb.toString();
    }
}
