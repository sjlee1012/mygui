package com.gmt.common.io.backup;

// ValueObject 클래스 정의
class ValueObject {
    public String fieldName;
    public String valueType;
    public String unit;
    public Object value;

    public ValueObject(String fieldName, String valueType, String unit, Object value) {
        this.fieldName = fieldName;
        this.valueType = valueType;
        this.unit = unit;
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "fieldName='" + fieldName + '\'' +
                ", valueType='" + valueType + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}
