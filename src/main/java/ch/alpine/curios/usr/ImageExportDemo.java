// code by jph
package ch.alpine.curios.usr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.Raster;
import ch.alpine.tensor.io.Export;
import ch.alpine.tensor.io.Import;
import ch.alpine.tensor.num.GaussScalar;

/* package */ enum ImageExportDemo {
  ;
  public static void _im1() throws Exception {
    int n = 251;
    Export.of(HomeDirectory.Pictures.resolve("image.png"), Tensors.matrix((i, j) -> GaussScalar.of(i * j, n), n, n));
  }

  public static void _im2() throws Exception {
    int n = 251;
    Export.of(HomeDirectory.Pictures.resolve("image2.png"), Tensors.matrix((i, j) -> //
    Tensors.of(RealScalar.of(i), RealScalar.of(j), GaussScalar.of(i + 2 * j, n), GaussScalar.of(i * j, n)), n, n));
  }

  public static void _im3() throws Exception {
    int n = 251;
    Tensor matrix = Tensors.matrix((i, j) -> //
    GaussScalar.of(i + 14 * j + i * i + i * j * 3, n), n, n);
    UnaryOperator<Scalar> asd = s -> RealScalar.of(s.number());
    matrix.maps(asd);
    Tensor image = Raster.of(matrix.maps(asd), ColorDataGradients.AURORA);
    Export.of(HomeDirectory.Pictures.resolve("image3.png"), image);
  }

  public static void jpg2gif() throws IOException {
    Path file = HomeDirectory.path("display.jpg");
    if (Files.isRegularFile(file)) {
      Tensor tensor = Import.of(file);
      Export.of(HomeDirectory.path("display.jpg.gif"), tensor);
    }
  }

  static void main() throws Exception {
    _im1();
    _im2();
    _im3();
    jpg2gif();
  }
}
