// code by jph
package ch.alpine.curios.man;

import java.awt.Container;
import java.util.Random;
import java.util.random.RandomGenerator;

import ch.alpine.bridge.fig.ListLinePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.crv.d2.alg.ConvexHull2D;
import ch.alpine.sophus.api.TangentSpace;
import ch.alpine.sophus.hs.st.StiefelManifold;
import ch.alpine.sophus.hs.st.TStMemberQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.lie.rot.CirclePoints;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
public class StExponentialDemo implements ManipulateProvider {
  private static final Tensor circle = CirclePoints.of(50);
  private static final Integer k = 2;
  @FieldSlider
  @FieldClip(min = "3", max = "12")
  public Integer n = 6;
  @FieldSlider
  @FieldClip(min = "-3", max = "30")
  public Scalar scalar = RealScalar.of(0);

  @Override
  public Container getContainer() {
    RandomGenerator randomGenerator = new Random(3);
    StiefelManifold stiefelManifold = new StiefelManifold(n, k);
    Tensor p = RandomSample.of(stiefelManifold, randomGenerator);
    Tensor v = new TStMemberQ(p).projection( //
        RandomVariate.of(NormalDistribution.of(0, 0.4), randomGenerator, Dimensions.of(p)));
    circle.append(circle.get(0));
    TangentSpace exponential = stiefelManifold.exponential(p);
    ScalarTensorFunction stf = s -> exponential.exp(v.multiply(s));
    Clip clip = Clips.translation(scalar).apply(Clips.absolute(4));
    Tensor res = Subdivide.increasing(clip, 50).maps(stf);
    // IO.println(Dimensions.of(res));
    Show show = new Show();
    show.add(ListLinePlot.of(circle));
    for (int i = 0; i < n; ++i)
      show.add(ListLinePlot.of(res.get(Tensor.ALL, Tensor.ALL, i)));
    Tensor ply = Transpose.of(res.get(0));
    Tensor hull = ConvexHull2D.of(ply);
    hull.append(hull.get(0));
    show.add(ListLinePlot.of(hull));
    show.setCbb(CoordinateBoundingBox.of(Clips.absoluteOne(), Clips.absoluteOne()));
    show.setAspectRatioOne();
    return ShowGridComponent.of(show);
  }

  static void main() {
    new StExponentialDemo().runStandalone();
  }
}
