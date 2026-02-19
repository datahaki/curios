// code by jph
package ch.alpine.curios.man;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.ReImPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.jet.EllipticCurve;
import ch.alpine.tensor.mat.UpperEvaluation;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
public class EllipticCurveExplorer implements ManipulateProvider {
  @FieldSelectionArray({ "{-1,0}", "{-1,1/4}", "{-1,1}", "{0,-2}", "{0,2}", "{0,5}", "{1,-1}" })
  public Tensor a_b = Tensors.vector(-1, +1);
  @FieldClip(min = "1", max = "5")
  public Integer depth = 3;

  @Override
  public JComponent getContainer() {
    Show show = new Show();
    EllipticCurve ellipticCurve = EllipticCurve.of(a_b.Get(0), a_b.Get(1));
    Tensor candidates = Subdivide.of(RealScalar.of(-8), RealScalar.of(8), 16 * 100);
    Tensor valid_roots = Tensors.empty();
    ellipticCurve.polynomial().roots().stream() //
        .map(Scalar.class::cast) //
        .filter(s -> s instanceof Rational) //
        .peek(valid_roots::append) //
        .forEach(candidates::append);
    Set<Tensor> set = new HashSet<>();
    for (Tensor _x : candidates) {
      Scalar x = (Scalar) _x;
      Scalar y = ellipticCurve.apply(x);
      if (y instanceof Rational) {
        Tensor point = Tensors.of(x, y);
        ellipticCurve.require(point);
        set.add(point);
      }
    }
    show.setPlotLabel(ellipticCurve.discriminant().toString() + "   roots=" + valid_roots);
    Clip clip = Clips.absolute(10);
    for (int i = 0; i < depth; ++i) {
      // TODO make discovery more efficient by avoiding repeating combos
      Tensor points = Tensor.of(set.stream());
      Tensor matrix = UpperEvaluation.of(points, points, ellipticCurve::combine, s -> s);
      set = Flatten.of(matrix, 1).stream() //
          .distinct().filter(Tensors::nonEmpty).collect(Collectors.toSet());
      // TODO points should be added as well!
    }
    show.add(ReImPlot.of(ellipticCurve, clip));
    show.add(ReImPlot.of(s -> ellipticCurve.apply(s).negate(), clip));
    Tensor points = Tensor.of(set.stream());
    show.add(ListPlot.of(points));
    show.setCbb(CoordinateBoundingBox.of(clip, Clips.absolute(8)));
    show.setAspectRatioOne();
    return ShowGridComponent.of(show);
  }

  static void main() {
    new EllipticCurveExplorer().run();
  }
}
