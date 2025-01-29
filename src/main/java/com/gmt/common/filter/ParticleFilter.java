package com.gmt.common.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleFilter {
    private List<Particle> particles; // 파티클 집합
    private int numParticles;         // 파티클 수
    private Random rand;

    // 프로세스 노이즈 표준편차(이동 시 적용할 노이즈)
    private double processStd;
    // 측정 노이즈 표준편차 (GPS 오차 가정)
    private double measurementStd;

    public ParticleFilter(int numParticles, double processStd, double measurementStd) {
        this.numParticles = numParticles;
        this.processStd = processStd;
        this.measurementStd = measurementStd;
        this.rand = new Random();

        this.particles = new ArrayList<>(numParticles);
    }

    /**
     * 초기 파티클 집합 생성
     * @param minX 파티클 초기 x 범위 최소
     * @param maxX 파티클 초기 x 범위 최대
     * @param minY 파티클 초기 y 범위 최소
     * @param maxY 파티클 초기 y 범위 최대
     */
    public void init(double minX, double maxX, double minY, double maxY) {
        particles.clear();
        for(int i=0; i<numParticles; i++) {
            double x = minX + rand.nextDouble()*(maxX-minX);
            double y = minY + rand.nextDouble()*(maxY-minY);
            double weight = 1.0 / numParticles; // 초기 가중치는 균등
            particles.add(new Particle(x, y, weight));
        }
    }

    /**
     * 예측 단계(Predict):
     * 파티클들을 동적 모델에 따라 이동 + 프로세스 노이즈 적용
     * 여기서는 단순히 "정지상태" + 노이즈만 적용 예시
     */
    public void predict() {
        for(Particle p : particles) {
            // 간단히 정지 모델 + 프로세스 노이즈만 적용
            // 실제로는 vx, vy 등이 있으면 p.x += vx + ...
            p.x += randGaussian(0, processStd);
            p.y += randGaussian(0, processStd);
        }
    }

    /**
     * 관측 갱신(Update):
     * GPS 관측값 (measX, measY)에 대해,
     * 각 파티클과의 오차를 계산하여 가중치 갱신
     */
    public void update(double measX, double measY) {
        double totalWeight = 0.0;
        for(Particle p : particles) {
            // 측정 모델: 실제 측정 = 파티클 위치
            // 오차 ~ N(0, measurementStd)
            // => 오차 pdf 값을 weight로 사용
            // 가우시안( x오차, y오차 )를 독립이라 가정
            double dx = (measX - p.x);
            double dy = (measY - p.y);
            double dist2 = dx*dx + dy*dy;
            // 2차원 가우시안 pdf(평균=0, 분산=measurementStd^2)
            // pdf = (1 / (2πσ^2)) * exp(-r^2 / (2σ^2))
            double sigma2 = measurementStd * measurementStd;
            double w = Math.exp(-dist2/(2*sigma2));
            // 상수 항 (1/(2πσ^2)) 은 비교시 동일하므로 생략 가능
            p.weight = w;
            totalWeight += w;
        }

        // 정규화
        if(totalWeight > 0) {
            for(Particle p : particles) {
                p.weight /= totalWeight;
            }
        } else {
            // 만약 totalWeight=0 => 모든 파티클이 관측값과 동떨어짐
            // 균등 분포로 재설정 (혹은 다른 로직)
            double equalW = 1.0 / particles.size();
            for(Particle p : particles) {
                p.weight = equalW;
            }
        }
    }

    /**
     * 리샘플(Resample):
     * 가중치가 높은 파티클 위주로 새로운 파티클 집합 형성
     * "루어 휠(Wheel)" 기법 또는 "시스템 리샘플링" 등 다양
     * 여기서는 간단한 "Low Variance Resampling" 예시
     */
    public void resample() {
        List<Particle> newParticles = new ArrayList<>(numParticles);
        double r = rand.nextDouble() * (1.0/numParticles);
        double c = p(0).weight; // 누적 가중치
        int i = 0;

        for(int m=0; m<numParticles; m++) {
            double U = r + (double)m / numParticles;
            while(U > c) {
                i++;
                c += p(i).weight;
            }
            // p(i)를 복제
            Particle src = p(i);
            newParticles.add(new Particle(src.x, src.y, src.weight));
        }
        this.particles = newParticles;
    }

    /**
     * 추정값(Estimate) 계산:
     * 모든 파티클의 가중평균으로 (x, y) 추정
     */
    public double[] estimate() {
        double ex=0, ey=0;
        for(Particle p : particles) {
            ex += p.x * p.weight;
            ey += p.y * p.weight;
        }
        return new double[]{ex, ey};
    }

    // 내부 유틸
    private Particle p(int idx) {
        // idx가 넘어가면 wrap-around
        return particles.get(idx % particles.size());
    }

    private double randGaussian(double mean, double std) {
        return mean + std*rand.nextGaussian();
    }

    public List<Particle> getParticles() {
        return particles;
    }
}

