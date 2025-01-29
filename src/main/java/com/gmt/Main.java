package com.gmt;

import com.gmt.common.geo.DangerZone;
import com.gmt.common.geo.SafeRoute;
import com.gmt.common.ship.Ship;
import com.gmt.common.scada.WindFarmManager;

import org.locationtech.jts.geom.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 매니저 생성
        WindFarmManager manager = new WindFarmManager();
        GeometryFactory gf = new GeometryFactory();

        // 1) DangerZone 예시: 임의의 사각형 (Polygon)
        //    좌표 (0,0)-(0,5)-(5,5)-(5,0)
        Coordinate[] coordsDZ = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(0,5),
                new Coordinate(5,5),
                new Coordinate(5,0),
                new Coordinate(0,0) // 닫기
        };
        Polygon dangerPoly = gf.createPolygon(coordsDZ);
        DangerZone dz1 = new DangerZone("DZ-1", dangerPoly);
        manager.addRegion(dz1);

        // 2) SafeRoute 예시: 라인스트링 + 폭 2.0
        //    라인: (10,0) -> (10,5) -> (15,5)
        Coordinate[] routeCoords = new Coordinate[]{
                new Coordinate(10, 0),
                new Coordinate(10, 5),
                new Coordinate(15, 5)
        };
        LineString routeLine = gf.createLineString(routeCoords);
        double routeWidth = 2.0; // 항로 폭 2.0 (실제로는 m 단위 등)
        SafeRoute sr1 = new SafeRoute("SR-1", routeLine, routeWidth);
        manager.addRegion(sr1);

        // 3) 선박 생성 (초기 좌표)
        Ship shipA = new Ship("Ship-A", -1, -1);

        // 이동 시뮬레이션
        moveAndCheck(manager, shipA, 1, 1);
        moveAndCheck(manager, shipA, 3, 3);
        moveAndCheck(manager, shipA, 10, 2);
        moveAndCheck(manager, shipA, 10, 4);
        moveAndCheck(manager, shipA, 14, 5);
        moveAndCheck(manager, shipA, 16, 5);
    }

    private static void moveAndCheck(WindFarmManager manager, Ship ship, double newX, double newY) {
        System.out.println("\n--- 선박 이동 ---");
        System.out.printf("%s -> (%.1f, %.1f)\n", ship.getShipId(), newX, newY);
        ship.move(newX, newY);

        // DangerZone 진입/이탈
        manager.checkDangerZoneEnterLeave(ship);

        // 현재 어떤 DangerZone에 있는지
        List<DangerZone> dzList = manager.checkDangerZones(ship);
        for (DangerZone dz : dzList) {
            System.out.printf("[경고] 선박 %s : 위험영역 %s 내부\n", ship.getShipId(), dz.getId());
        }

        // 현재 어떤 SafeRoute에 있는지
        List<SafeRoute> srList = manager.checkSafeRoutes(ship);
        for (SafeRoute sr : srList) {
            System.out.printf("[안전] 선박 %s : 안전항로 %s 위에 있음\n", ship.getShipId(), sr.getId());
        }
    }
}

