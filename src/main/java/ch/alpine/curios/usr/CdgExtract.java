// code by jph
package ch.alpine.curios.usr;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.IntStream;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.plt.ListLinePlot;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.img.ColorDataLists;
import ch.alpine.tensor.io.Export;
import ch.alpine.tensor.io.Import;

class CdgExtract implements ShowProvider {
  @Override
  public Show getShow() {
    Show show = new Show(ColorDataLists._109.strict());
    try {
      Tensor tensor = Import.of(HomeDirectory.Pictures.resolve("vectorplot.png")).get(0);
      // tensor = tensor.extract(1, tensor.length());
      tensor.set(Tensors.vector(10, 0, 178, 255), 0);
      tensor.set(Tensors.vector(255, 189, 0, 255), tensor.length() - 1);
      // Tensor domain = Subdivide.of(0,255, tensor.length()-1);
      Tensor domain = Range.of(0, tensor.length());
      IO.println(Dimensions.of(tensor));
      String[] rgb = { "R", "G", "B" };
      for (int i = 0; i < 3; ++i)
        show.add(ListLinePlot.of(domain, tensor.get(Tensor.ALL, i))).setLabel(rgb[i]);
      int[] indeces = new int[] { 0, 33, 66, 99, 133, 166, 198 };
      IO.println(tensor.length());
      Tensor rgba = Tensor.of(IntStream.of(indeces).mapToObj(tensor::get));
      IO.println(rgba);
      Path path = HomeDirectory.Projects.resolve("tensor", //
          "src/main/resources/ch/alpine/tensor/img/colorscheme", "sunset2.csv");
      Export.of(path, rgba);
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    return show;
  }

  static void main() {
    new CdgExtract().runStandalone();
  }
}
