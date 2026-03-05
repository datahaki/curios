// code by jph
package ch.alpine.curios.exp;

import ch.alpine.sophis.flow.StateSpaceModel;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.exp.Exp;

enum DEqs implements StateSpaceModel {
  EXP_DECAY {
    @Override
    public Tensor f(Tensor x, Tensor u) {
      return x.negate();
    }

    @Override
    ScalarUnaryOperator exact() {
      return t -> Exp.FUNCTION.apply(t.negate());
    }
  };

  abstract ScalarUnaryOperator exact();
}
