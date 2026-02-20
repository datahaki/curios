package ch.alpine.subare.demo.net;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.Ramp;
import ch.alpine.tensor.sca.UnitStep;
import ch.alpine.tensor.sca.exp.DLogisticSigmoid;
import ch.alpine.tensor.sca.exp.LogisticSigmoid;

public class LinearLayer implements Layer {
  public static LinearLayer logSig(Distribution d, int ante, int post) {
    LinearLayer linearLayer = new LinearLayer();
    linearLayer.f = LogisticSigmoid.FUNCTION;
    linearLayer.df = DLogisticSigmoid.NESTED;
    linearLayer.W = RandomVariate.of(d, ante, post);
    linearLayer.b = Array.zeros(post);
    return linearLayer;
  }

  public static LinearLayer reLu(Distribution d, int ante, int post) {
    LinearLayer linearLayer = new LinearLayer();
    linearLayer.f = Ramp.FUNCTION;
    linearLayer.df = UnitStep.FUNCTION;
    linearLayer.W = RandomVariate.of(d, ante, post);
    linearLayer.b = Array.zeros(post);
    return linearLayer;
  }

  public static LinearLayer maxE(Distribution d, int ante, int post) {
    LinearLayer linearLayer = new LinearLayer();
    linearLayer.f = s -> s;
    linearLayer.df = UnitStep.FUNCTION;
    linearLayer.W = RandomVariate.of(d, ante, post);
    linearLayer.b = Array.zeros(post);
    return linearLayer;
  }

  ScalarUnaryOperator f;
  ScalarUnaryOperator df;
  Tensor W;
  Tensor b;
  Tensor x;
  Tensor d;

  @Override
  public Tensor forward(Tensor x) {
    this.x = x;
    return x.dot(W).add(b).maps(f);
  }

  @Override
  public Tensor back(Tensor d) {
    this.d = d;
    return Entrywise.mul().apply(W.dot(d), x.maps(df));
  }

  @Override
  public void update() {
    W = W.add(TensorProduct.of(x, d));
    b = b.add(d);
  }

  @Override
  public Tensor error(Tensor y) {
    throw new RuntimeException();
  }

  @Override
  public Tensor parameters() {
    return Flatten.of(W, b);
  }
}
