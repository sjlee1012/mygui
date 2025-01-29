package com.gmt.common.scada;

class CommProtocol {
    private String protocolName; // ex: IEC61850, OPC UA, Modbus

    public CommProtocol(String protocolName) {
        this.protocolName = protocolName;
    }
}
