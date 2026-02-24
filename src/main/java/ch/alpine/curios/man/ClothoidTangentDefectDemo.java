package ch.alpine.curios.man;

import java.awt.Container;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.crv.clt.ClothoidTangentDefect;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class ClothoidTangentDefectDemo implements ManipulateProvider {
  @FieldSlider
  @FieldClip(min = "-1", max = "1")
  public Scalar s1 = RealScalar.of(0);
  @FieldSlider
  @FieldClip(min = "-1", max = "1")
  public Scalar s2 = RealScalar.of(0);

  @Override
  public Container getContainer() {
    ClothoidTangentDefect clothoidTangentDefect = ClothoidTangentDefect.of(s1, s2);
    Show show = new Show();
    show.add(Plot.of(clothoidTangentDefect::defect, Clips.absolute(15.0)));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new ClothoidTangentDefectDemo().runStandalone();
  }
}
