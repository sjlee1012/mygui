package com.gmt;


import com.gmt.common.iec61162.DeviceProfileLoader;
import com.gmt.common.iec61162.message.GgaMessage;
import com.gmt.common.iec61162.message.NmeaMessage;
import com.gmt.common.iec61162.NmeaMessageFactory;

public class MainApp {
    public static void main(String[] args) {
        // 1) 장치별 지원 메시지 설정(YAML) 로딩
        //    "/devices.yaml"는 resources 폴더에 위치했다고 가정
        DeviceProfileLoader.loadProfilesFromYaml("/devices.yaml");

        // 이제 DeviceProfileRegistry에 DeviceA, DeviceB, DeviceC 프로필이 등록됨

        // 2) NMEA 예시 문장
        String ggaSentence = "$GPGGA,123456.00,3714.2803,N,12701.5260,E,1,08,1.09,117.2,M,17.8,M,,*5B";

        // 3) "DeviceA"에서 들어온 메시지 파싱
        NmeaMessage msgA = NmeaMessageFactory.create(ggaSentence, "DeviceA");
        if (msgA instanceof GgaMessage) {
            GgaMessage gga = (GgaMessage) msgA;
            System.out.println("[DeviceA] GGA time: " + gga.getTime());
            System.out.println("[DeviceA] lat/lon : " + gga.getLatitude() + ", " + gga.getLongitude());
        }

        // 4) "DeviceB"에서 들어온 메시지 파싱
        NmeaMessage msgB = NmeaMessageFactory.create(ggaSentence, "DeviceB");
        if (msgB instanceof GgaMessage) {
            GgaMessage gga2 = (GgaMessage) msgB;
            System.out.println("[DeviceB] GGA time: " + gga2.getTime());
            System.out.println("[DeviceB] lat/lon : " + gga2.getLatitude() + ", " + gga2.getLongitude());
        }

        // 5) 만약 DeviceB가 "GSA" 메시지를 지원하지 않는다고 YAML에 설정된 경우:
        String gsaSentence = "$GPGSA,.....";
        try {
            NmeaMessage msgB2 = NmeaMessageFactory.create(gsaSentence, "DeviceB");
            // UnsupportedOperationException 발생 가능
        } catch (Exception e) {
            System.out.println("[DeviceB] GSA => " + e.getMessage());
        }
    }
}
