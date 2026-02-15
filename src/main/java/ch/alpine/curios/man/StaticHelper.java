// code by jph
package ch.alpine.curios.man;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.ResourceData;
import ch.alpine.tensor.io.ImageFormat;

enum StaticHelper {
  ;
  public static final Tensor IMAGE = ImageFormat.from( //
      ResourceData.bufferedImage("/ch/alpine/curios/man/ca54d607.png")).unmodifiable();
}
