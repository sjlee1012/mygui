package com.gmt;

import com.gmt.common.filter.KalmanFilter2D;

public class TestKalman2DCommons {
    public static void main(String[] args) {
        double dt = 1.0; // 1초 간격
        KalmanFilter2D kf = new KalmanFilter2D(dt);

        // 초기 상태 설정(0,0,0,0)
        kf.setState(0, 0, 0, 0);

        // 실제 선박이 (x=0, y=0)에서 시작해 vx=1, vy=0.5 로 이동한다고 가정
        double realX = 0;
        double realY = 0;
        double vx = 1.0;
        double vy = 0.5;

        for(int t=0; t<20; t++) {
            // 실제 이동
            realX += vx*dt;
            realY += vy*dt;

            // GPS 노이즈 (평균 0, 표준편차=100)
            double gpsNoiseX = 100.0*(Math.random()-0.5)*2;
            double gpsNoiseY = 100.0*(Math.random()-0.5)*2;

            double measX = realX + gpsNoiseX;
            double measY = realY + gpsNoiseY;

            // 1) 예측
            kf.predict();

            // 2) 관측 업데이트
            kf.update(measX, measY);

            double[] est = kf.getState();
            System.out.printf("T=%2d | Real=(%.2f,%.2f) | Meas=(%.2f,%.2f) | Est=(%.2f,%.2f)\n",
                    t, realX, realY, measX, measY, est[0], est[1]);
        }
    }
}
