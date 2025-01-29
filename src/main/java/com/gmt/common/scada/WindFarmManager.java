package com.gmt.common.scada;


import com.gmt.common.geo.DangerZone;
import com.gmt.common.geo.Region;
import com.gmt.common.geo.SafeRoute;
import com.gmt.common.ship.Ship;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.index.strtree.STRtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WindFarmManager {

    private STRtree strTree;
    private Map<String, Region> regionMap; // id -> Region

    private GeometryFactory geomFactory;

    public WindFarmManager() {
        this.strTree = new STRtree();
        this.regionMap = new HashMap<>();
        this.geomFactory = new GeometryFactory();
    }

    public void addRegion(Region region) {
        // Region ID 중복 방지 로직(간단 처리)
        if (regionMap.containsKey(region.getId())) {
            throw new IllegalArgumentException("중복된 Region ID: " + region.getId());
        }

        // R-Tree 삽입
        Geometry g = region.getGeometry();
        strTree.insert(g.getEnvelopeInternal(), region);
        regionMap.put(region.getId(), region);
    }

    public void removeRegion(String regionId) {
        Region region = regionMap.get(regionId);
        if (region != null) {
            strTree.remove(region.getGeometry().getEnvelopeInternal(), region);
            regionMap.remove(regionId);
        }
    }

    /**
     * 선박이 현재 어떤 위험영역(DangerZone)에 있는지 확인
     */
    public List<DangerZone> checkDangerZones(Ship ship) {
        Point shipPoint = geomFactory.createPoint(new Coordinate(ship.getX(), ship.getY()));
        Envelope shipEnv = shipPoint.getEnvelopeInternal();

        @SuppressWarnings("unchecked")
        List<Region> candidates = strTree.query(shipEnv);

        List<DangerZone> result = new ArrayList<>();
        for (Region r : candidates) {
            if (r instanceof DangerZone) {
                if (r.getGeometry().intersects(shipPoint)) {
                    result.add((DangerZone) r);
                }
            }
        }
        return result;
    }

    /**
     * 선박이 현재 어떤 안전항로(SafeRoute)에 있는지 확인
     */
    public List<SafeRoute> checkSafeRoutes(Ship ship) {
        Point shipPoint = geomFactory.createPoint(new Coordinate(ship.getX(), ship.getY()));
        Envelope shipEnv = shipPoint.getEnvelopeInternal();

        @SuppressWarnings("unchecked")
        List<Region> candidates = strTree.query(shipEnv);

        List<SafeRoute> result = new ArrayList<>();
        for (Region r : candidates) {
            if (r instanceof SafeRoute) {
                if (r.getGeometry().contains(shipPoint)) {
                    result.add((SafeRoute) r);
                }
            }
        }
        return result;
    }

    /**
     * 선박 이동 후, 이전 위치 ~ 현재 위치 비교하여
     * DangerZone 진입/이탈 이벤트 파악 (단순 예시)
     */
    public void checkDangerZoneEnterLeave(Ship ship) {
        // 이전 위치에서 intersect 하던 DangerZone 목록
        Point prevPoint = geomFactory.createPoint(new Coordinate(ship.getPrevX(), ship.getPrevY()));
        Envelope prevEnv = prevPoint.getEnvelopeInternal();
        @SuppressWarnings("unchecked")
        List<Region> prevCandidates = strTree.query(prevEnv);
        List<String> prevInDanger = new ArrayList<>();

        for (Region r : prevCandidates) {
            if (r instanceof DangerZone) {
                if (r.getGeometry().intersects(prevPoint)) {
                    prevInDanger.add(r.getId());
                }
            }
        }

        // 현재 위치에서 intersect 하던 DangerZone 목록
        Point currPoint = geomFactory.createPoint(new Coordinate(ship.getX(), ship.getY()));
        Envelope currEnv = currPoint.getEnvelopeInternal();
        @SuppressWarnings("unchecked")
        List<Region> currCandidates = strTree.query(currEnv);
        List<String> currInDanger = new ArrayList<>();

        for (Region r : currCandidates) {
            if (r instanceof DangerZone) {
                if (r.getGeometry().intersects(currPoint)) {
                    currInDanger.add(r.getId());
                }
            }
        }

        // 진입: 이전에는 없었는데 현재에 있다
        for (String dzId : currInDanger) {
            if (!prevInDanger.contains(dzId)) {
                System.out.printf("[진입] 선박 %s -> 위험영역 '%s'\n", ship.getShipId(), dzId);
            }
        }

        // 이탈: 이전에는 있었는데 현재는 없다
        for (String dzId : prevInDanger) {
            if (!currInDanger.contains(dzId)) {
                System.out.printf("[이탈] 선박 %s -> 위험영역 '%s'\n", ship.getShipId(), dzId);
            }
        }
    }


    /**
     * (간단 예시) 선박 이동 후 "안전항로(SafeRoute)" 진입/이탈 상태를 파악하여 Drools Facts를 갱신
     */
    public void checkSafeRouteEnterLeave(Ship ship) {
        Point currPoint = geomFactory.createPoint(new Coordinate(ship.getX(), ship.getY()));
        Envelope currEnv = currPoint.getEnvelopeInternal();

        @SuppressWarnings("unchecked")
        List<Region> candidates = strTree.query(currEnv);

        // 현재 선박이 들어있는 SafeRoute 목록
        List<String> currentSafeRoutes = new ArrayList<>();

        for (Region r : candidates) {
            if (r instanceof SafeRoute) {
                // 라인스트링 버퍼(Polygon) 안에 있는지 검사
                if (r.getGeometry().contains(currPoint)) {
                    currentSafeRoutes.add(r.getId());
                }
            }
        }

        // 기존에 이 선박이 "inside=true" 로 등록해둔 항로 목록
        // shipZoneFacts 에서 shipId 매칭되는 것만 필터
//        List<String> previouslyInside = new ArrayList<>();
//        for (ShipInSafeZoneFact fact : shipZoneFacts.values()) {
//            if (fact.getShipId().equals(ship.getShipId()) && fact.isInside()) {
//                previouslyInside.add(fact.getZoneId());
//            }
//        }

//        // (1) 새로 진입한 항로 = (currentSafeRoutes - previouslyInside)
//        for (String zoneId : currentSafeRoutes) {
//            if (!previouslyInside.contains(zoneId)) {
//                // 새로 진입
//                System.out.printf("[진입] 선박 %s -> 안전항로 '%s'\n", ship.getShipId(), zoneId);
//
//                // Drools Fact 생성/등록
//                ShipInSafeZoneFact fact = new ShipInSafeZoneFact(
//                        ship.getShipId(),
//                        zoneId,
//                        System.currentTimeMillis(),
//                        true
//                );
//                // (shipId + zoneId) 키로 관리
//                String key = fact.getShipId() + "_" + fact.getZoneId();
//                shipZoneFacts.put(key, fact);
//
//                // KieSession에 insert
//                kieSession.insert(fact);
//            }
//        }
//
//        // (2) 이탈한 항로 = (previouslyInside - currentSafeRoutes)
//        for (String zoneId : previouslyInside) {
//            if (!currentSafeRoutes.contains(zoneId)) {
//                // 이탈
//                System.out.printf("[이탈] 선박 %s -> 안전항로 '%s'\n", ship.getShipId(), zoneId);
//
//                // Fact 업데이트 (inside=false)
//                String key = ship.getShipId() + "_" + zoneId;
//                ShipInSafeZoneFact existingFact = shipZoneFacts.get(key);
//                if (existingFact != null && existingFact.isInside()) {
//                    existingFact.setInside(false);
//                    // Drools에서 fact 수정 시 update() 필요
//                    kieSession.update(kieSession.getFactHandle(existingFact), existingFact);
//                }
//            }
//        }
//
//        // 모든 변경사항을 적용한 뒤, 룰 발화(실행)
//        kieSession.fireAllRules();
    }

}
