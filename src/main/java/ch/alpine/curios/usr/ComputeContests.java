// code by jph
package ch.alpine.curios.usr;

import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.mat.ev.RealEigensystem;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.ex.MatrixPower;
import ch.alpine.tensor.mat.ex.MatrixSqrt;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.mat.gr.Mahalanobis;
import ch.alpine.tensor.mat.pi.PseudoInverse;
import ch.alpine.tensor.mat.qr.GramSchmidt;
import ch.alpine.tensor.mat.qr.QRDecomposition;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Timing;

public enum ComputeContests {
  MAT_MAT("serial", "parallel") {
    @Override
    void runTrials(int n, Timing s_ser, Timing s_par) {
      Distribution distribution = NormalDistribution.standard();
      Tensor a = RandomVariate.of(distribution, n, n);
      Tensor b = RandomVariate.of(distribution, n, n);
      s_ser.start();
      Tensor cs = a.dot(b);
      s_ser.stop();
      s_par.start();
      Tensor cp = Parallelize.dot(a, b);
      s_par.stop();
      if (!Tolerance.CHOP.isClose(cs, cp))
        throw new Throw(cs);
    }
  },
  MAT_VEC("serial", "parallel") {
    @Override
    void runTrials(int n, Timing s_ser, Timing s_par) {
      Distribution distribution = NormalDistribution.standard();
      Tensor a = RandomVariate.of(distribution, n, n);
      Tensor b = RandomVariate.of(distribution, n);
      s_ser.start();
      Tensor cs = a.dot(b);
      s_ser.stop();
      s_par.start();
      Tensor cp = Parallelize.dot(a, b);
      s_par.stop();
      if (!Tolerance.CHOP.isClose(cs, cp))
        throw new Throw(cs);
    }
  },
  QR_GS("qr", "gs") {
    @Override
    void runTrials(int n, Timing s_ser, Timing s_par) {
      Distribution distribution = NormalDistribution.standard();
      Tensor a = RandomVariate.of(distribution, n, n);
      s_ser.start();
      QRDecomposition.of(a);
      s_ser.stop();
      s_par.start();
      GramSchmidt.of(a);
      s_par.stop();
    }
  },
  INV_PINV("inv", "pinv") {
    @Override
    void runTrials(int n, Timing s_ser, Timing s_par) {
      Distribution distribution = NormalDistribution.standard();
      Tensor a = RandomVariate.of(distribution, n, n);
      s_ser.start();
      Inverse.of(a);
      s_ser.stop();
      s_par.start();
      PseudoInverse.of(a);
      s_par.stop();
    }
  },
  INF_MAH("inf", "mah") {
    @Override
    void runTrials(int n, Timing s_ser, Timing s_par) {
      Distribution distribution = NormalDistribution.standard();
      Tensor a = RandomVariate.of(distribution, n, Math.max(1, n / 2 - 3));
      s_ser.start();
      InfluenceMatrix.of(a).matrix();
      s_ser.stop();
      s_par.start();
      new Mahalanobis(a).matrix();
      s_par.stop();
    }
  },
  EIGSYS("sym", "real") {
    @Override
    void runTrials(int n, Timing s_ser, Timing s_par) {
      Distribution distribution = NormalDistribution.of(1, 4);
      Tensor a = Symmetrize.of(RandomVariate.of(distribution, n, n));
      s_ser.start();
      Eigensystem.ofSymmetric(a);
      s_ser.stop();
      s_par.start();
      RealEigensystem.of(a);
      s_par.stop();
    }
  },
  EXP_LOG("exp", "log") {
    @Override
    void runTrials(int n, Timing s_ser, Timing s_par) {
      Distribution distribution = NormalDistribution.of(0, 0.01);
      Tensor a = RandomVariate.of(distribution, n, n);
      s_ser.start();
      Tensor exp = MatrixExp.of(a);
      s_ser.stop();
      s_par.start();
      MatrixLog.of(exp);
      s_par.stop();
    }
  },
  SQR_POW("sqr", "pow") {
    @Override
    void runTrials(int n, Timing s_ser, Timing s_par) {
      Distribution distribution = NormalDistribution.of(0, 0.01);
      Tensor a = IdentityMatrix.inplaceAdd(RandomVariate.of(distribution, n, n));
      a = a.dot(a);
      s_ser.start();
      MatrixSqrt.of(a);
      s_ser.stop();
      s_par.start();
      MatrixPower.of(a, 0.6);
      s_par.stop();
    }
  };

  public final String label1;
  public final String label2;

  private ComputeContests(String label1, String label2) {
    this.label1 = label1;
    this.label2 = label2;
  }

  abstract void runTrials(int n, Timing s_ser, Timing s_par);
}
