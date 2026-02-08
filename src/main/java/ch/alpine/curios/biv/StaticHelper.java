// code by jph
package ch.alpine.curios.biv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.img.ColorDataGradient;
import ch.alpine.tensor.img.Raster;
import ch.alpine.tensor.io.Export;

/* package */ enum StaticHelper {
  ;
  private static final int GALLERY_RES = 720; // 128 + 64;

  public static Path file(Class<?> cls) {
    Path DIRECTORY = HomeDirectory.Pictures.resolve("tensor");
    try {
      Files.createDirectories(DIRECTORY);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return DIRECTORY.resolve(cls.getSimpleName() + ".png");
  }

  public static void export(BivariateEvaluation bivariateEvaluation, Class<?> cls, ColorDataGradient colorDataGradient) throws IOException {
    Export.of(file(cls), Raster.of(image(bivariateEvaluation, GALLERY_RES), colorDataGradient));
  }

  public static Tensor image(BivariateEvaluation bivariateEvaluation, int resolution) {
    Tensor re = Subdivide.increasing(bivariateEvaluation.clipX(), resolution - 1);
    Tensor im = Subdivide.increasing(bivariateEvaluation.clipY(), resolution - 1);
    return Parallelize.matrix((i, j) -> bivariateEvaluation.apply(re.Get(j), im.Get(i)), resolution, resolution);
  }

  public static Tensor image(BivariateEvaluation bivariateEvaluation) {
    return image(bivariateEvaluation, GALLERY_RES);
  }

  public static Tensor raster(BivariateEvaluation bivariateEvaluation, ColorDataGradient colorDataGradient) {
    return Raster.of(image(bivariateEvaluation, GALLERY_RES), colorDataGradient);
  }
}
