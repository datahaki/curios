// code by jph
package ch.alpine.curios.klotzki;

import java.io.Serializable;
import java.util.List;

import ch.alpine.owl.math.state.StateTime;
import ch.alpine.tensor.Tensor;

record KlotskiSolution(KlotskiProblem klotskiProblem, List<StateTime> list, Tensor domain) //
    implements Serializable {
}
