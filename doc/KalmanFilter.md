아래 예시는 **GPS 오차를 줄이기 위한 간단한 칼만 필터(Kalman Filter)**를 **자바(Java)**로 구현한 예시입니다. 실제 해상도나 환경에 맞춰 더 정교한 모델링(가속도, 회전, 파고 등)을 고려할 수 있지만, 여기서는 **2차원 위치+속도** 상태를 추적하는 **기본적인 선형 칼만 필터**를 예시로 설명하겠습니다.

---

# 1. 칼만 필터 개념 요약

1. **상태 벡터(State Vector)**
    - 이번 예시에서는 2차원 이동 물체(선박) 위치 \(\mathbf{x}, \mathbf{y}\)와 속도 \(\mathbf{v_x}, \mathbf{v_y}\)를 추적합니다.
    - \(\mathbf{x}_k = [x, y, v_x, v_y]^T\)

2. **상태 방정식(State Transition)**  
   시간 간격 \(\Delta t\)에서 선박이 등속 이동한다고 가정하면,  
   \[
   \begin{aligned}
   x_{k+1} &= x_k + v_{x,k} \Delta t \\
   y_{k+1} &= y_k + v_{y,k} \Delta t \\
   v_{x, k+1} &= v_{x, k} \\
   v_{y, k+1} &= v_{y, k}
   \end{aligned}
   \]

   이를 행렬 형태로 표현하면,
   \[
   \mathbf{x}_{k+1} = A \mathbf{x}_k + \mathbf{w}_k
   \]
   여기서 \(\mathbf{w}_k\)는 프로세스 잡음(Process Noise).  
   \[
   A = \begin{bmatrix}
   1 & 0 & \Delta t & 0 \\
   0 & 1 & 0 & \Delta t \\
   0 & 0 & 1 & 0 \\
   0 & 0 & 0 & 1
   \end{bmatrix}
   \]

3. **관측(Measurement) 방정식**
    - GPS는 위치 \([x, y]\)만 측정한다고 가정.
    - 측정 벡터 \(\mathbf{z}_k = [x_{GPS}, y_{GPS}]\).
    - 속도는 직접 측정하지 않음.
    - 관측 행렬 \(H\)는 다음과 같음:
      \[
      \mathbf{z}_k = H \mathbf{x}_k + \mathbf{v}_k, \quad
      H = \begin{bmatrix}
      1 & 0 & 0 & 0 \\
      0 & 1 & 0 & 0
      \end{bmatrix}
      \]
      \(\mathbf{v}_k\)는 측정 잡음(Measurement Noise).

4. **공분산 행렬**
    - 프로세스 잡음 공분산 \(Q\): 모델의 불확실성(선박이 등속으로 가지 않을 수도 있음)
    - 측정 잡음 공분산 \(R\): GPS 측정 노이즈(예: 1σ가 10~100m 정도)

5. **칼만 필터 절차**
    1) **예측 단계 (Predict)**  
       \[
       \hat{\mathbf{x}}_{k|k-1} = A \hat{\mathbf{x}}_{k-1|k-1}
       \]  
       \[
       P_{k|k-1} = A P_{k-1|k-1} A^T + Q
       \]
    2) **갱신 단계 (Update)**  
       \[
       K_k = P_{k|k-1} H^T \big(H P_{k|k-1} H^T + R\big)^{-1}
       \]  
       \[
       \hat{\mathbf{x}}_{k|k} = \hat{\mathbf{x}}_{k|k-1} + K_k \big(\mathbf{z}_k - H \hat{\mathbf{x}}_{k|k-1}\big)
       \]  
       \[
       P_{k|k} = \big(I - K_k H\big) P_{k|k-1}
       \]

---

# 2. 자바 구현 예시

아래 클래스는 **간단한 2D 칼만 필터**로, **위치(x, y)와 속도(vx, vy)**를 추정합니다.

> **주의**: 행렬 연산을 간단하게 직접 코딩했으나, 실제로는 [EJML](https://github.com/lessthanoptimal/ejml), [Apache Commons Math](https://commons.apache.org/proper/commons-math/) 같은 **행렬 라이브러리**를 사용하는 것이 안전하고 유지보수에 좋습니다.

```java
package example.kalman;

public class KalmanFilter2D {
    // 상태벡터 크기: 4 (x, y, vx, vy)
    private static final int STATE_DIM = 4;
    // 측정벡터 크기: 2 (x, y)
    private static final int MEAS_DIM = 2;

    // 상태 추정치: [x, y, vx, vy] (4x1 벡터)
    private double[] x;

    // 오차 공분산 행렬 P (4x4)
    private double[][] P;

    // 상태전이 행렬 A (4x4)
    private double[][] A;

    // 관측 행렬 H (2x4)
    private double[][] H;

    // 프로세스 잡음 공분산 Q (4x4)
    private double[][] Q;

    // 측정 잡음 공분산 R (2x2)
    private double[][] R;

    // 단위 행렬 I (4x4)
    private double[][] I;

    // 시간간격 Δt (초 단위)
    private double deltaT;

    public KalmanFilter2D(double deltaT) {
        this.deltaT = deltaT;

        // 상태 추정 초기화
        this.x = new double[] {0, 0, 0, 0}; // x, y, vx, vy

        // P 초기화 (초기에 큰 불확실성 가정)
        this.P = new double[][] {
                {1000, 0,    0,    0},
                {0,    1000, 0,    0},
                {0,    0,    1000, 0},
                {0,    0,    0,    1000}
        };

        // A 초기화 (등속 모델)
        this.A = new double[][] {
                {1, 0, deltaT, 0},
                {0, 1, 0,      deltaT},
                {0, 0, 1,      0},
                {0, 0, 0,      1}
        };

        // H 초기화 (측정은 x, y만)
        this.H = new double[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0}
        };

        // Q (프로세스 잡음), 모델 불확실성
        // 여기서는 단순히 vx, vy에 조금씩 잡음이 있다고 가정
        double accelStd = 1.0; // 가속도 표준편차 예시
        double dt2 = deltaT * deltaT;
        // Q는 (가속도 오차)^2 * [dt^4/4, dt^3/2, dt^2] 형태로 구성 가능
        // 간단히 대각선에 값 부여 (예: 1,1,5,5)
        this.Q = new double[][] {
                {dt2*accelStd, 0,              0,              0},
                {0,            dt2*accelStd,   0,              0},
                {0,            0,              deltaT*accelStd,0},
                {0,            0,              0,              deltaT*accelStd}
        };

        // R (측정 잡음), GPS 오차 100m 정도 가정 => 표준편차 100
        double gpsStd = 100.0; 
        this.R = new double[][] {
                {gpsStd * gpsStd, 0},
                {0, gpsStd * gpsStd}
        };

        // I (4x4) 단위행렬
        this.I = new double[][] {
                {1,0,0,0},
                {0,1,0,0},
                {0,0,1,0},
                {0,0,0,1}
        };
    }

    // 상태 벡터 x,y,vx,vy 초기값 설정
    public void setState(double x, double y, double vx, double vy) {
        this.x[0] = x;
        this.x[1] = y;
        this.x[2] = vx;
        this.x[3] = vy;
    }

    // 예측 단계 (Predict)
    public void predict() {
        // x = A * x
        this.x = multiply(A, this.x);

        // P = A * P * A^T + Q
        this.P = matAdd(matMul(A, this.P, A, true), Q);
    }

    // 관측 업데이트 (z = [x_meas, y_meas])
    public void update(double measX, double measY) {
        double[] z = new double[] { measX, measY };
        // S = H * P * H^T + R
        double[][] S = matAdd(matMul(H, P, H, true), R);

        // K = P * H^T * S^-1
        double[][] PHt = matMul(P, H, true); // (4x2)
        double[][] SInv = matInv(S);
        double[][] K = matMul(PHt, SInv); // (4x2) x (2x2) => (4x2)

        // y = z - H*x
        double[] hx = multiply(H, x); // 측정 예측값
        double[] yVec = new double[] {
                z[0] - hx[0],
                z[1] - hx[1]
        };

        // x = x + K*y
        double[] Ky = multiply(K, yVec); // (4x2)*(2x1) => (4x1)
        for(int i=0; i<4; i++) {
            x[i] += Ky[i];
        }

        // P = (I - K*H) * P
        double[][] KH = matMul(K, H); // (4x2)*(2x4) => (4x4)
        double[][] IminusKH = matSub(I, KH);
        this.P = matMul(IminusKH, P);
    }

    // 현재 추정 상태 반환 (x, y, vx, vy)
    public double[] getState() {
        return x; 
    }

    /* ==============================
     * 아래는 간단한 행렬 연산 유틸
     * ============================== */

    // 행렬 x 벡터 곱
    private double[] multiply(double[][] M, double[] v) {
        int rows = M.length;
        int cols = M[0].length;
        double[] out = new double[rows];
        for (int r=0; r<rows; r++) {
            double sum = 0;
            for(int c=0; c<cols; c++) {
                sum += M[r][c] * v[c];
            }
            out[r] = sum;
        }
        return out;
    }

    // 행렬 x 행렬 x 행렬^T 형태 (C = A*B*A^T 와 유사) -> 편의 함수
    private double[][] matMul(double[][] A, double[][] B, double[][] A2, boolean transposeA2) {
        // 첫 번째: M1 = A*B
        double[][] M1 = matMul(A, B);
        // 두 번째: M2 = M1*A2^T
        double[][] A2T = transposeA2 ? matTranspose(A2) : A2;
        return matMul(M1, A2T);
    }

    // 행렬 곱 (C = A * B)
    private double[][] matMul(double[][] A, double[][] B) {
        int aRows = A.length;
        int aCols = A[0].length;
        int bRows = B.length;
        int bCols = B[0].length;
        if(aCols != bRows) throw new RuntimeException("Dimension mismatch");

        double[][] C = new double[aRows][bCols];
        for(int i=0; i<aRows; i++) {
            for(int j=0; j<bCols; j++) {
                double sum=0;
                for(int k=0; k<aCols; k++) {
                    sum += A[i][k]*B[k][j];
                }
                C[i][j] = sum;
            }
        }
        return C;
    }

    // 행렬 - 행렬
    private double[][] matSub(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] C = new double[rows][cols];
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    // 행렬 + 행렬
    private double[][] matAdd(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] C = new double[rows][cols];
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    // 전치
    private double[][] matTranspose(double[][] M) {
        int rows = M.length;
        int cols = M[0].length;
        double[][] T = new double[cols][rows];
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                T[j][i] = M[i][j];
            }
        }
        return T;
    }

    // 행렬 역행렬 (2x2 전용 간단 버전)
    private double[][] matInv(double[][] M) {
        // 이 예시는 (2x2) R, S 같은 작은 행렬에만 사용 (안전한 범위)
        if(M.length == 2 && M[0].length == 2) {
            double det = M[0][0]*M[1][1] - M[0][1]*M[1][0];
            if(Math.abs(det) < 1e-9) throw new RuntimeException("Matrix is singular");
            double invDet = 1.0/det;
            return new double[][] {
                    { M[1][1]*invDet, -M[0][1]*invDet },
                    { -M[1][0]*invDet, M[0][0]*invDet }
            };
        } else {
            // 실제로는 4x4 역행렬 등 더 일반화된 방법 필요
            throw new RuntimeException("Inverse only implemented for 2x2");
        }
    }
}
```

---

## 3. 사용 예시

```java
package example.kalman;

public class TestKalman2D {
    public static void main(String[] args) {
        double dt = 1.0; // 1초 간격
        KalmanFilter2D kf = new KalmanFilter2D(dt);

        // 초기 위치 (0,0), 초기 속도 (0,0) 가정
        kf.setState(0, 0, 0, 0);

        // 실제 선박은 (x=0, y=0)에서 시작, vx=1 m/s, vy=0.5 m/s로 이동한다고 가정
        // GPS 측정치는 100m 정도 오차가 난다고 가정
        double realX = 0;
        double realY = 0;
        double vx = 1.0;
        double vy = 0.5;

        for(int t=0; t<20; t++) {
            // 실제 이동
            realX += vx * dt;
            realY += vy * dt;

            // GPS 측정 오차를 랜덤 시뮬레이션 (평균0, 표준편차=100)
            double gpsNoiseX = 100.0 * (Math.random() - 0.5) * 2; 
            double gpsNoiseY = 100.0 * (Math.random() - 0.5) * 2;

            double measX = realX + gpsNoiseX;
            double measY = realY + gpsNoiseY;

            // 1) 예측
            kf.predict();
            // 2) 업데이트 (측정값 반영)
            kf.update(measX, measY);

            // 추정값 출력
            double[] est = kf.getState();
            System.out.printf("Time=%d sec | Real=(%.2f,%.2f) | Meas=(%.2f,%.2f) | Est=(%.2f,%.2f)\n",
                    t, realX, realY, measX, measY, est[0], est[1]);
        }
    }
}
```

실행하면 매 시점마다,

- **Real**: 실제 위치
- **Meas**: GPS로부터 측정된 노이즈 포함 위치
- **Est**: 칼만 필터가 추정한 위치

를 출력합니다. 오차가 큰 측정값이 들어와도, 칼만 필터가 **속도와 이전 추정치**를 참고하여 추정값(Est)을 안정화시킬 것입니다.

---

# 4. 개선 및 확장 아이디어

1. **가속도(Acceleration) 반영**
    - 선박이 속도만이 아니라 **가속/감속**이 있다면, 상태 벡터에 가속도 항을 추가하거나 확장 칼만 필터(EKF)를 사용할 수 있습니다.

2. **Non-linear 상황**
    - 실제 선박은 회전이나 조류에 의해 비선형 움직임을 하므로, **EKF(Extended Kalman Filter)** 혹은 **UKF(Unscented Kalman Filter)**를 고려하세요.

3. **다중 센서 융합**
    - IMU, 자이로, 해상레이더, RTK-GPS 등을 결합하면 정확성을 높일 수 있습니다.

4. **행렬 라이브러리 활용**
    - 간단한 예시로는 위처럼 직접 행렬 연산을 코딩해도 되지만, 실무에서는 **Apache Commons Math** 또는 **EJML**을 사용해 역행렬, 곱셈 등을 안정적으로 처리하시길 권장합니다.

---

## 5. 결론

위 코드는 **2D 칼만 필터**를 이용해 **GPS 오차**를 스무딩하고, **위치/속도**를 추정하는 **기본 예시**입니다. 실제 프로젝트에서는 **센서 특성, 해양 환경, 추가 센서** 등을 고려해 잡음 모델 \(Q, R\)을 조정하고, **EKF/UKF** 등 비선형 칼만 필터를 적용해 정확도를 향상시키면 좋습니다.

이처럼 칼만 필터를 통해 GPS 측정치가 튀거나 노이즈가 있어도, “더 그럴듯한” 추정 위치를 실시간으로 계산할 수 있어, **선박의 위험 구역 진입/이탈 판단**을 보다 안정적으로 수행할 수 있습니다.