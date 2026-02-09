// code by jph
package ch.alpine.curios.fig;

import java.awt.image.BufferedImage;

import ch.alpine.bridge.fig.ImagePlot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.sophis.crv.clt.ClothoidBuilder;
import ch.alpine.sophis.crv.clt.ClothoidBuilders;
import ch.alpine.sophis.crv.clt.LagrangeQuadraticD;
import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.Raster;
import ch.alpine.tensor.io.ImageFormat;

record ClothoidFigure(Scalar angle) implements ShowProvider {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();
  private static final int RES = 192;
  private static final Tensor RE = Subdivide.of(-1, +1, RES - 1);
  private static final Tensor IM = Subdivide.of(+0.1, +2.1, RES - 1);
  // ---

  private Scalar function(int y, int x) {
    Tensor q = Tensors.of(RE.Get(x), IM.Get(y), angle);
    LagrangeQuadraticD headTailInterface = CLOTHOID_BUILDER.curve(q.maps(Scalar::zero), q).curvature();
    return headTailInterface.maxAbs().reciprocal();
  }

  @Override
  public Show getShow() {
    Tensor matrix = Parallelize.matrix(this::function, RES, RES);
    Tensor image = Raster.of(matrix, ColorDataGradients.SUNSET);
    BufferedImage bufferedImage = ImageFormat.of(image);
    Show show = new Show();
    show.add(ImagePlot.of(bufferedImage));
    return show;
  }

  static void main() {
    new ClothoidFigure(RealScalar.of(2.6)).run();
  }
}
