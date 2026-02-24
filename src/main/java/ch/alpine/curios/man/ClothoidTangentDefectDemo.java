// code by jph
package ch.alpine.curios.man;

import java.awt.Container;
import java.util.LinkedList;
import java.util.List;

import ch.alpine.bridge.fig.ListPlot;
import ch.alpine.bridge.fig.ReImPlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.StringPlot;
import ch.alpine.bridge.fig.StringPlot.StringItem;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.crv.clt.ClothoidSolutions;
import ch.alpine.sophis.crv.clt.ClothoidTangentDefect;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Round;

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
    Clip clip = Clips.absolute(15.0);
    ClothoidTangentDefect clothoidTangentDefect = ClothoidTangentDefect.of(s1, s2);
    ClothoidSolutions clothoidSolutions = new ClothoidSolutions(clothoidTangentDefect, clip);
    Show show = new Show();
    show.add(ReImPlot.of(clothoidTangentDefect, clip));
    Tensor zeros = Tensor.of(clothoidSolutions.lambdas().stream().map(l -> Tensors.of(l, l.maps(Scalar::zero))));
    show.add(ListPlot.of(zeros));
    List<StringItem> list = new LinkedList<>();
    for (Tensor _l : clothoidSolutions.lambdas()) {
      Scalar l = (Scalar) _l;
      list.add(StringItem.of(Tensors.of(l, l.zero()), "" + l.maps(Round._4)));
    }
    show.add(StringPlot.of(list));
    return ShowGridComponent.of(show);
  }

  static void main() {
    new ClothoidTangentDefectDemo().runStandalone();
  }
}
