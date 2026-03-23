// code by jph
package ch.alpine.curios.fig;

import java.awt.Container;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.Showable;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldFuse;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.se.SeNGroup;
import ch.alpine.sophus.lie.sl.SlNGroup;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Accumulate;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;
import ch.alpine.tensor.sca.exp.Log10;

@ReflectionMarker
class BchConvergenceShow implements ManipulateProvider {
  @FieldSlider
  @FieldClip(min = "1", max = "12")
  public Integer depth = 1;
  @FieldFuse
  public transient Boolean shuffle = false;

  Showable add(String name, MatrixAlgebra matrixAlgebra) {
    Tensor tensor = err(matrixAlgebra);
    Showable showable = ListLinePlot.of(Range.of(0, tensor.length()), tensor.maps(Log10.FUNCTION));
    showable.setLabel(name);
    return showable;
  }

  private Tensor err(MatrixAlgebra matrixAlgebra) {
    Tensor ad = matrixAlgebra.ad().maps(N.DOUBLE);
    BakerCampbellHausdorff bakerCampbellHausdorff = //
        new BakerCampbellHausdorff(ad, depth, Chop._15);
    Tensor x = Tensors.vector(+0.10, +0.12, +0.07);
    Tensor y = Tensors.vector(+0.05, -0.06, +0.11);
    Tensor series = bakerCampbellHausdorff.series(x, y);
    Tensor X = matrixAlgebra.toMatrix(x);
    Tensor Y = matrixAlgebra.toMatrix(y);
    Tensor Z = MatrixLog.of(MatrixExp.of(X).dot(MatrixExp.of(Y)));
    Tensor ref = matrixAlgebra.toVector(Z);
    return Tensor.of(Accumulate.of(series).stream().map(val -> val.subtract(ref)).map(FrobeniusNorm::of));
  }

  @Override
  public Container getContainer() {
    Show show = new Show();
    show.setShowLabel("bch convergence depth=" + depth);
    show.add(add("se2", MatrixAlgebra.of(new SeNGroup(2))));
    show.add(add("so3", MatrixAlgebra.of(new SoNGroup(3))));
    show.add(add("sl2", MatrixAlgebra.of(new SlNGroup(2))));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new BchConvergenceShow().runStandalone();
  }
}
