package com.gmt.common.io.backup;

import java.util.*;


public class MyGGA implements MyNmeaObject {

    private Map<String, ValueObject> hash;
    private List<ValueObject> list;

    MyGGA(){
        hash = new HashMap<>();
        list = new ArrayList<>();
    }

    // 필드명과 값 오브젝트를 해시에 저장하고 리스트로 순서 참조
    @Override
    public void parseToHashAndList(String inputSentence) {
        // 입력 문자열 파싱
        String[] tokens = inputSentence.split(",");
        if (!tokens[0].equals("$GPGGA")) {
            throw new IllegalArgumentException("Invalid GGA sentence");
        }

        String hhmmss = tokens[1];
        float latitude = parseCoordinate(tokens[2], tokens[3]);
        float longitude = parseCoordinate(tokens[4], tokens[5]);
        int quality = Integer.parseInt(tokens[6]);
        int numberSat = Integer.parseInt(tokens[7]);
        float hdop = Float.parseFloat(tokens[8]);
        float altitude = Float.parseFloat(tokens[9]);
        float geoSeparation = Float.parseFloat(tokens[11]);

        ValueObject hhmmssObj = new ValueObject("hhmmss", "String", null, hhmmss);
        ValueObject latitudeObj = new ValueObject("latitude", "float", "degrees", latitude);
        ValueObject longitudeObj = new ValueObject("longitude", "float", "degrees", longitude);
        ValueObject qualityObj = new ValueObject("quality", "int", null, quality);
        ValueObject numberSatObj = new ValueObject("numberSat", "int", null, numberSat);
        ValueObject hdopObj = new ValueObject("hdop", "float", null, hdop);
        ValueObject altitudeObj = new ValueObject("altitude", "float", "meters", altitude);
        ValueObject geoSeparationObj = new ValueObject("geoSeparation", "float", "meters", geoSeparation);

        // 해시에 저장
        hash.put("hhmmss", hhmmssObj);
        hash.put("latitude", latitudeObj);
        hash.put("longitude", longitudeObj);
        hash.put("quality", qualityObj);
        hash.put("numberSat", numberSatObj);
        hash.put("hdop", hdopObj);
        hash.put("altitude", altitudeObj);
        hash.put("geoSeparation", geoSeparationObj);

        // 리스트에 순서대로 저장
        list.add(hhmmssObj);
        list.add(latitudeObj);
        list.add(longitudeObj);
        list.add(qualityObj);
        list.add(numberSatObj);
        list.add(hdopObj);
        list.add(altitudeObj);
        list.add(geoSeparationObj);
    }


    private float parseCoordinate(String value, String direction) {
        float coordinate = Float.parseFloat(value.substring(0, 2)) + Float.parseFloat(value.substring(2)) / 60;
        if ("S".equals(direction) || "W".equals(direction)) {
            coordinate = -coordinate;
        }
        return coordinate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MyGGA{");
        sb.append("hash=").append(hash).append(", list=").append(list).append('}');
        return sb.toString();
    }

    public static void main(String[] args) {
        String inputSentence = "$GPGGA,070004.90,3724.2506,N,12706.0901,E,1,6,2.2,252.8,M,21.7,M,,*50";
        MyGGA gga = new MyGGA();
        gga.parseToHashAndList(inputSentence);

        System.out.println("Hash Map:");
        gga.hash.forEach((key, value) -> System.out.println(key + " -> " + value));

        System.out.println("\nList:");
        gga.list.forEach(System.out::println);
    }
}
