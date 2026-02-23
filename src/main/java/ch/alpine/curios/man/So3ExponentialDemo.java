// code by jph
package ch.alpine.curios.man;

import java.awt.Container;
import java.io.ByteArrayOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophus.lie.so.So3Exponential;
import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.ext.AnimatedGifWriter;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.Raster;
import ch.alpine.tensor.io.ImageFormat;

@ReflectionMarker
enum So3ExponentialDemo implements ManipulateProvider {
  INSTANCE;

  private static final int RES = 192;
  private static final Tensor RE = Subdivide.of(-4, +4, RES - 1);
  private static final Tensor IM = Subdivide.of(-4, +4, RES - 1);
  @FieldClip(min = "10", max = "1000")
  public Integer millis = 100;

  record Slice(Scalar Z) {
    Scalar function(int y, int x) {
      Tensor mat = So3Exponential.vectorExp(Tensors.of(RE.Get(x), IM.Get(y), Z));
      return mat.Get(0, 2);
    }
  }

  @Override
  public Container getContainer() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (AnimatedGifWriter animationWriter = //
        AnimatedGifWriter.of(baos, millis, true)) {
      for (Tensor _z : Subdivide.of(-4 * Math.PI, 4 * Math.PI, 10)) {
        IO.print(".");
        Slice slice = new Slice((Scalar) _z);
        Tensor matrix = Parallelize.matrix(slice::function, RES, RES);
        animationWriter.write(ImageFormat.of(Raster.of(matrix, ColorDataGradients.CLASSIC)));
      }
      IO.println();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    ImageIcon imageIcon = new ImageIcon(baos.toByteArray());
    return new JLabel(imageIcon);
  }

  static void main() {
    // TODO BRIDGE when value was changed... than window close launches listener again!
    INSTANCE.runStandalone();
  }
}
