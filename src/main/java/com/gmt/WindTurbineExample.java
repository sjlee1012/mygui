package com.gmt;

import com.gmt.domain.generation.generator.Blade;
import com.gmt.domain.generation.generator.GearBox;
import com.gmt.domain.generation.generator.Generator;
import com.gmt.domain.generation.substation.PowerConverter;
import com.gmt.domain.generation.generator.WindTurbine;

public class WindTurbineExample {

    public static void main(String[] args) {
        // 블레이드 3개를 가진 풍력발전기를 예시로 생성
        Blade blade1 = new Blade("Blade-1", 60.0, 0.0);
        Blade blade2 = new Blade("Blade-2", 60.0, 0.0);
        Blade blade3 = new Blade("Blade-3", 60.0, 0.0);

        GearBox gearBox = new GearBox("GearBox-1", 0.0, 40.0, 70.0);
        Generator generator = new Generator("Generator-1", 0.0, 0.0);
        PowerConverter converter = new PowerConverter("Converter-1", 0.0, 0.0);

        // WindTurbine 객체 생성
        WindTurbine windTurbine = new WindTurbine(
                "Turbine-1",
                new Blade[]{blade1, blade2, blade3},
                gearBox,
                generator,
                converter
        );

        // 시뮬레이션: 풍속에 따라 블레이드 회전속도 증가
        double windSpeed = 12.0; // 풍속 12 m/s 가정
        windTurbine.updateTurbineState(windSpeed);

        // 풍력발전기의 현재 상태 출력
        System.out.println(windTurbine);

        // 특정 시점에 블레이드 회전 중단(예: 비상 정지 상황)
        windTurbine.stopBlades();
        System.out.println("\n--- 비상 정지 후 상태 ---");
        System.out.println(windTurbine);
    }
}

