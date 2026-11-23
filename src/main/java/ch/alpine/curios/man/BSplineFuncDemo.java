// code by jph
package ch.alpine.curios.man;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowGridComponent;
import ch.alpine.bridge.fig.plt.ListPlot;
import ch.alpine.bridge.fig.plt.Plot;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldSelectionArray;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.itp.BSplineFunctionString;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class BSplineFuncDemo implements ManipulateProvider {
  public Integer seed = 0;
  @FieldSelectionArray({ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" })
  public Integer degree = 1;
  public Integer width = 5;

  @Override
  public JComponent getContainer() {
    List<Show> list = new LinkedList<>();
    Random random = new Random(seed);
    Tensor tensor = RandomVariate.of(NormalDistribution.standard(), random, width + 1);
    {
      Show show = new Show();
      show.add(ListPlot.of(Range.of(0, tensor.length()), tensor));
      for (int _deg = 0; _deg < degree; ++_deg) {
        ScalarTensorFunction stf = BSplineFunctionString.of(_deg, tensor);
        show.add(Plot.of(s -> (Scalar) stf.apply(s), Clips.positive(width)));
      }
      list.add(show);
    }
    return ShowGridComponent.of(list);
  }

  static void main() {
    new BSplineFuncDemo().runStandalone();
  }
}
