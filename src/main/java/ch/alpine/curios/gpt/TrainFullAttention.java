// code by jph
package ch.alpine.curios.gpt;

import java.util.Random;

public class TrainFullAttention {
  static Random rnd = new Random(1);
  static final int D = 2;
  static final double LR = 0.01;
  // Trainable weights
  static double[][] Wq = randomMatrix(D, D);
  static double[][] Wk = randomMatrix(D, D);
  static double[][] Wv = randomMatrix(D, D);

  public static void main(String[] args) {
    System.out.println("Before training:");
    evaluate();
    train(10000);
    System.out.println("\nAfter training:");
    evaluate();
  }

  // =====================================================
  // TRAINING
  // =====================================================
  static void train(int epochs) {
    for (int epoch = 0; epoch < epochs; epoch++) {
      // Two random tokens
      double[][] x = { { rnd.nextDouble(), rnd.nextDouble() }, { rnd.nextDouble(), rnd.nextDouble() } };
      // Target = larger first feature
      double target = Math.max(x[0][0], x[1][0]);
      Forward f = forward(x);
      double pred = f.output;
      // MSE loss
      double loss = 0.5 * (pred - target) * (pred - target);
      // dLoss/dOutput
      double dOut = pred - target;
      // =================================================
      // BACKPROP
      // =================================================
      // attended[0] -> output
      double[] dAttended = { dOut, 0 };
      // Gradients wrt values and attention weights
      double[][] dValues = new double[2][D];
      double[] dWeights = new double[2];
      for (int i = 0; i < 2; i++) {
        // attended += w_i * value_i
        for (int j = 0; j < D; j++) {
          dValues[i][j] += dAttended[j] * f.weights[i];
        }
        // d weight
        dWeights[i] += dot(dAttended, f.values[i]);
      }
      // =================================================
      // SOFTMAX BACKPROP
      // =================================================
      double[] dScores = new double[2];
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          double grad;
          if (i == j) {
            grad = f.weights[i] * (1 - f.weights[j]);
          } else {
            grad = -f.weights[i] * f.weights[j];
          }
          dScores[i] += dWeights[j] * grad;
        }
      }
      // scaling
      for (int i = 0; i < 2; i++) {
        dScores[i] /= Math.sqrt(D);
      }
      // =================================================
      // SCORE = q dot k
      // =================================================
      double[] dQ = new double[D];
      double[][] dK = new double[2][D];
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < D; j++) {
          dQ[j] += dScores[i] * f.keys[i][j];
          dK[i][j] += dScores[i] * f.query[j];
        }
      }
      // =================================================
      // UPDATE Wv
      // =================================================
      for (int i = 0; i < D; i++) {
        for (int j = 0; j < D; j++) {
          double grad = 0;
          for (int t = 0; t < 2; t++) {
            grad += dValues[t][i] * x[t][j];
          }
          Wv[i][j] -= LR * grad;
        }
      }
      // =================================================
      // UPDATE Wq
      // =================================================
      for (int i = 0; i < D; i++) {
        for (int j = 0; j < D; j++) {
          double grad = dQ[i] * x[0][j];
          Wq[i][j] -= LR * grad;
        }
      }
      // =================================================
      // UPDATE Wk
      // =================================================
      for (int i = 0; i < D; i++) {
        for (int j = 0; j < D; j++) {
          double grad = 0;
          for (int t = 0; t < 2; t++) {
            grad += dK[t][i] * x[t][j];
            Wk[i][j] -= LR * grad;
          }
        }
      }
      if (epoch % 1000 == 0) {
        System.out.printf("epoch=%d loss=%.6f pred=%.4f target=%.4f%n", epoch, loss, pred, target);
      }
    }
  }

  // =====================================================
  // FORWARD
  // =====================================================
  static Forward forward(double[][] x) {
    Forward f = new Forward();
    // First token becomes query
    f.query = matVec(Wq, x[0]);
    f.keys = new double[2][];
    f.values = new double[2][];
    for (int i = 0; i < 2; i++) {
      f.keys[i] = matVec(Wk, x[i]);
      f.values[i] = matVec(Wv, x[i]);
    }
    double[] scores = new double[2];
    for (int i = 0; i < 2; i++) {
      scores[i] = dot(f.query, f.keys[i]) / Math.sqrt(D);
    }
    f.weights = softmax(scores);
    double[] attended = new double[D];
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < D; j++) {
        attended[j] += f.weights[i] * f.values[i][j];
      }
    }
    f.output = attended[0];
    return f;
  }

  // =====================================================
  // EVALUATION
  // =====================================================
  static void evaluate() {
    for (int n = 0; n < 5; n++) {
      double[][] x = { { rnd.nextDouble(), rnd.nextDouble() }, { rnd.nextDouble(), rnd.nextDouble() } };
      double target = Math.max(x[0][0], x[1][0]);
      Forward f = forward(x);
      System.out.printf("a=%.2f b=%.2f target=%.2f pred=%.2f attn=[%.2f %.2f]%n", x[0][0], x[1][0], target, f.output, f.weights[0], f.weights[1]);
    }
  }

  // =====================================================
  // HELPERS
  // =====================================================
  static class Forward {
    double[] query;
    double[][] keys;
    double[][] values;
    double[] weights;
    double output;
  }

  static double[][] randomMatrix(int r, int c) {
    double[][] m = new double[r][c];
    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {
        m[i][j] = rnd.nextGaussian() * 0.1;
      }
    }
    return m;
  }

  static double[] matVec(double[][] m, double[] v) {
    double[] out = new double[m.length];
    for (int i = 0; i < m.length; i++) {
      for (int j = 0; j < v.length; j++) {
        out[i] += m[i][j] * v[j];
      }
    }
    return out;
  }

  static double dot(double[] a, double[] b) {
    double s = 0;
    for (int i = 0; i < a.length; i++) {
      s += a[i] * b[i];
    }
    return s;
  }

  static double[] softmax(double[] x) {
    double max = Double.NEGATIVE_INFINITY;
    for (double v : x) {
      if (v > max)
        max = v;
    }
    double sum = 0;
    double[] e = new double[x.length];
    for (int i = 0; i < x.length; i++) {
      e[i] = Math.exp(x[i] - max);
      sum += e[i];
    }
    for (int i = 0; i < x.length; i++) {
      e[i] /= sum;
    }
    return e;
  }
}
