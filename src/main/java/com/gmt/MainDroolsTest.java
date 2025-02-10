package com.gmt;


import com.gmt.common.ship.Ship;
import com.gmt.common.scada.WindFarmManager;

public class MainDroolsTest {
    public static void main(String[] args) throws InterruptedException {
        WindFarmManager manager = new WindFarmManager();

        // 1) 안전항로(SafeRoute) 등록 (예: 라인스트링 + buffer)
        // 여기서는 간단히 사각형 폴리곤 등으로 대체 가능
        // (좌표 정의는 생략하거나 기존 예시 참고)
        // ...
//         manager.addRegion( new SafeRoute("SR-1", lineString, 50.0) );
//         manager.addRegion( new SafeRoute("SR-2", lineString2, 100.0) );

        // 2) 선박 생성
        Ship shipA = new Ship("Ship-A", 0, 0);

        // (가정) 안전항로 "SR-1" 범위가 (0,0)~(10,10)라고 치고,
        // 선박을 그 내부로 이동시키면 "진입" 이벤트 -> Drools Fact 생성
        System.out.println("\n--- 이동 #1: (5,5) ---");
        shipA.move(5, 5);
        manager.checkSafeRouteEnterLeave(shipA);

        // 10분보다 짧은 시간 후 (예: 3초 후) 이동 -> 아직 10분 경과 안됨
        Thread.sleep(3000);

        System.out.println("\n--- 이동 #2: (6,6) ---");
        shipA.move(6, 6);
        manager.checkSafeRouteEnterLeave(shipA);

        // (테스트용) 10분 경과 시뮬레이션: 10분 = 600,000 ms
        Thread.sleep(6000); // 실제 10분은 너무 길므로 6초 등으로 테스트

        // 다시 한번 안전항로 내부에서 이동 or 같은 위치라도 checkSafeRouteEnterLeave() 호출
        System.out.println("\n--- 이동 #3: (7,7) ---");
        shipA.move(7, 7);
        manager.checkSafeRouteEnterLeave(shipA);

        // 이 시점에서 Drools 룰이 "(현재시간 - enterTime >= 600,000)" 조건을 만족하면
        // 콘솔에 알람 메시지 출력
    }
}

