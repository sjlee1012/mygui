package com.gmt.common.decision;

//import org.kie.api.KieServices;
//import org.kie.api.runtime.KieContainer;
//import org.kie.api.runtime.KieSession;
//
//public class DroolsUtil {
//
//    private static KieSession kieSession;
//
//    static {
//        try {
//            KieServices ks = KieServices.Factory.get();
//            KieContainer kContainer = ks.getKieClasspathContainer();
//            // "ksession-rules" 는 kmodule.xml 내의 <kbase>/<ksession> 설정 이름
//            kieSession = kContainer.newKieSession("ksession-rules");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static KieSession getKieSession() {
//        return kieSession;
//    }
//}
