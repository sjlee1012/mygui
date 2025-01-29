package com.gmt;


import com.gmt.common.filter.ParticleFilter;

public class TestParticleFilter {
    public static void main(String[] args) {
        // 파티클 개수, 프로세스 노이즈, 측정 노이즈
        ParticleFilter pf = new ParticleFilter(1000, 0.5, 10.0);

        // 초기 파티클: x in [0..100], y in [0..100]
        pf.init(0, 100, 0, 100);

        // 실제 선박 위치 (0,0)에서 시작
        double realX = 0;
        double realY = 0;
        // 여기서는 선박이 vx=1 m/s, vy=0.5 로 움직인다고 가정
        double vx = 1.0;
        double vy = 0.5;

        // 시뮬레이션
        for(int t=0; t<20; t++) {
            // 1) 선박 실제 이동
            realX += vx;
            realY += vy;

            // 2) 파티클 필터 예측
            pf.predict();

            // 3) GPS 측정(노이즈: 측정 표준편차=10m)
            //    ParticleFilter 생성자에서 measurementStd=10.0으로 설정
            //    실제로는 update()에서 weighting에 사용
            double gpsNoiseX = (Math.random()-0.5)*2*10.0; // ±10
            double gpsNoiseY = (Math.random()-0.5)*2*10.0;
            double measX = realX + gpsNoiseX;
            double measY = realY + gpsNoiseY;

            // 4) 관측 갱신
            pf.update(measX, measY);

            // 5) 리샘플
            pf.resample();

            // 6) 추정
            double[] est = pf.estimate();
            System.out.printf("Time=%2d | Real=(%.2f,%.2f) | Meas=(%.2f,%.2f) | Est=(%.2f,%.2f)\n",
                    t, realX, realY, measX, measY, est[0], est[1]);
        }
    }
}
