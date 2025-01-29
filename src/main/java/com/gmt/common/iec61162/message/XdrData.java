package com.gmt.common.iec61162.message;

public class XdrData {
    private String type;  // "A", "T", "C" 등
    private double data;  // 측정값
    private String unit;  // "D", "C", "Bar" 등
    private String transducerId; // "ROLL", "PTCH" 등

    public XdrData(String type, double data, String unit, String transducerId) {
        this.type = type;
        this.data = data;
        this.unit = unit;
        this.transducerId = transducerId;
    }

    public String getType() {
        return type;
    }
    public double getData() {
        return data;
    }
    public String getUnit() {
        return unit;
    }
    public String getTransducerId() {
        return transducerId;
    }

    @Override
    public String toString() {
        return "XdrData{" +
                "type='" + type + '\'' +
                ", data=" + data +
                ", unit='" + unit + '\'' +
                ", transducerId='" + transducerId + '\'' +
                '}';
    }
}
