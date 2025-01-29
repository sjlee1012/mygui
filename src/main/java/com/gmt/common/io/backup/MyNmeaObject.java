package com.gmt.common.io.backup;

public interface MyNmeaObject {
    // 필드명과 값 오브젝트를 해시에 저장하고 리스트로 순서 참조
    void parseToHashAndList(String inputSentence);

}
