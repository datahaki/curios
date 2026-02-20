package ch.alpine.subare.demo.net;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.exp.DLogisticSigmoid;
import ch.alpine.tensor.sca.exp.LogisticSigmoid;

public class LinearLayer {
  public static LinearLayer logSig(Distribution d, int ante, int post) {
    LinearLayer linearLayer = new LinearLayer();
    linearLayer.f = LogisticSigmoid.FUNCTION;
    linearLayer.df = DLogisticSigmoid.NESTED;
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

  public Tensor forward(Tensor x) {
    this.x = x;
    return x.dot(W).add(b).maps(f);
  }

  public Tensor back(Tensor d2) {
    return d = Entrywise.mul().apply(W.dot(d2), x.maps(df));
  }

  public void update(Tensor d) {
    W = W.add(TensorProduct.of(x, d));
    b = b.add(d);
  }
}
