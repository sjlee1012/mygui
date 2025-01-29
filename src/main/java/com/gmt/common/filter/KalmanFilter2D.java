package com.gmt.common.filter;


import org.apache.commons.math3.linear.*;

public class KalmanFilter2D {
    // 상태 벡터 크기: (x, y, vx, vy) => 4
    private final int stateDim = 4;
    // 측정 벡터 크기: (x, y) => 2
    private final int measDim = 2;

    // 시간 간격 Δt
    private double deltaT;

    // 상태 추정치 x (4x1)
    private RealVector x;

    // 오차 공분산 행렬 P (4x4)
    private RealMatrix P;

    // 상태전이 행렬 A (4x4)
    private RealMatrix A;

    // 관측 행렬 H (2x4)
    private RealMatrix H;

    // 프로세스 잡음 공분산 Q (4x4)
    private RealMatrix Q;

    // 측정 잡음 공분산 R (2x2)
    private RealMatrix R;

    // 단위 행렬 I (4x4)
    private RealMatrix I;

    /**
     * @param deltaT  시뮬레이션(관측) 간격 (초)
     */
    public KalmanFilter2D(double deltaT) {
        this.deltaT = deltaT;
        initMatrices();
    }

    private void initMatrices() {
        // 초기 상태 벡터 (0,0,0,0)
        this.x = new ArrayRealVector(new double[]{0.0, 0.0, 0.0, 0.0});

        // P 초기화 (처음에는 큰 불확실성)
        this.P = MatrixUtils.createRealDiagonalMatrix(new double[]{1000, 1000, 1000, 1000});

        // A 초기화 (등속도 모델)
        // [1   0   dt  0]
        // [0   1   0   dt]
        // [0   0   1   0 ]
        // [0   0   0   1 ]
        this.A = MatrixUtils.createRealMatrix(new double[][] {
                {1, 0, deltaT, 0},
                {0, 1, 0, deltaT},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });

        // H (관측은 x,y만)
        // [1 0 0 0]
        // [0 1 0 0]
        this.H = MatrixUtils.createRealMatrix(new double[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0}
        });

        // 프로세스 잡음 공분산 Q
        // 예: 가속도 오차 1.0 기준으로 단순히 대각선에 값 배치 (개선 여지 많음)
        double accelStd = 1.0;
        double dt2 = deltaT*deltaT;
        // 아주 단순한 Q 예시
        this.Q = MatrixUtils.createRealMatrix(new double[][] {
                {dt2*accelStd, 0,             0,              0},
                {0,            dt2*accelStd,  0,              0},
                {0,            0,             deltaT*accelStd,0},
                {0,            0,             0,              deltaT*accelStd}
        });

        // 측정 잡음 공분산 R
        // GPS 오차가 대략 100m => 표준편차=100
        double gpsStd = 100.0;
        double gpsVar = gpsStd * gpsStd; // 100^2 = 10000
        this.R = MatrixUtils.createRealMatrix(new double[][] {
                {gpsVar, 0},
                {0, gpsVar}
        });

        // I (4x4) 단위 행렬
        this.I = MatrixUtils.createRealIdentityMatrix(stateDim);
    }

    /**
     * 상태 초기값 설정 (ex: 선박 시작위치/속도)
     */
    public void setState(double x0, double y0, double vx0, double vy0) {
        this.x.setEntry(0, x0);
        this.x.setEntry(1, y0);
        this.x.setEntry(2, vx0);
        this.x.setEntry(3, vy0);
    }

    /**
     * 예측 단계 (Predict):
     *   x(k|k-1) = A x(k-1|k-1)
     *   P(k|k-1) = A P(k-1|k-1) A^T + Q
     */
    public void predict() {
        // x = A * x
        this.x = A.operate(this.x);
        // P = A P A^T + Q
        this.P = A.multiply(P).multiply(A.transpose()).add(Q);
    }

    /**
     * 관측 업데이트 (Update):
     *   z = [x_gps, y_gps]
     *   K = P(k|k-1) H^T [H P H^T + R]^-1
     *   x(k|k) = x(k|k-1) + K [z - H x(k|k-1)]
     *   P(k|k) = (I - K H) P(k|k-1)
     */
    public void update(double measX, double measY) {
        // z (2x1)
        RealVector z = new ArrayRealVector(new double[]{measX, measY});

        // S = H P H^T + R (2x2)
        RealMatrix S = H.multiply(P).multiply(H.transpose()).add(R);

        // K = P H^T S^-1 (4x2)
        RealMatrix K = P.multiply(H.transpose()).multiply(new LUDecomposition(S).getSolver().getInverse());

        // y = z - H x
        RealVector y = z.subtract(H.operate(x));

        // x = x + K y
        RealVector Ky = K.operate(y);
        x = x.add(Ky);

        // P = (I - K H) P
        RealMatrix KH = K.multiply(H);
        RealMatrix IminusKH = I.subtract(KH);
        P = IminusKH.multiply(P);
    }

    /**
     * 현재 추정 상태 반환: [x, y, vx, vy]
     */
    public double[] getState() {
        return x.toArray();
    }

    /**
     * 현재 오차 공분산 행렬 반환
     */
    public RealMatrix getCovariance() {
        return P.copy();
    }
}
