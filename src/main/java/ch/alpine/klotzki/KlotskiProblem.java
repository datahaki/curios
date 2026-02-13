// code by jph
package ch.alpine.klotzki;

import java.io.Serializable;

import ch.alpine.owl.glc.core.StateTimeRaster;
import ch.alpine.tensor.Tensor;

record KlotskiProblem( //
    Tensor startState, String name, StateTimeRaster stateTimeRaster, Tensor size, Tensor goal, Tensor frame, Tensor border) //
    implements Serializable {
}
