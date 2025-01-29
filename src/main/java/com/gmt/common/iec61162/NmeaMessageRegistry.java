package com.gmt.common.iec61162;

import com.gmt.common.iec61162.message.GgaMessage;
import com.gmt.common.iec61162.message.NmeaMessage;

import java.util.HashMap;
import java.util.Map;

public class NmeaMessageRegistry {

    // "GGA" → GgaMessage.class
    // "RMC" → RmcMessage.class, etc...
    private static final Map<String, Class<? extends NmeaMessage>> REGISTRY = new HashMap<>();

    static {
        // 초기 등록
        register("GGA", GgaMessage.class);
        // register("RMC", RmcMessage.class);
        // register("GSV", GsvMessage.class);
        // ...
    }

    public static void register(String messageType, Class<? extends NmeaMessage> clazz) {
        REGISTRY.put(messageType, clazz);
    }

    public static Class<? extends NmeaMessage> getMessageClass(String messageType) {
        return REGISTRY.get(messageType);
    }
}
