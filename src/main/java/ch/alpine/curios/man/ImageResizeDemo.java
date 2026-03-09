// code by jph
package ch.alpine.curios.man;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JComponent;

import ch.alpine.bridge.awt.ScalableImage;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.ext.ResourceData;
import ch.alpine.tensor.img.ImageResize;

@ReflectionMarker
public class ImageResizeDemo implements ManipulateProvider {
  private final Map<ImageResize, ScalableImage> map = new EnumMap<>(ImageResize.class);
  @FieldSlider
  @FieldClip(min = "0.1", max = "5")
  public Scalar magnify = RealScalar.of(1);

  public ImageResizeDemo() {
    BufferedImage bufferedImage = ResourceData.bufferedImage("ch/alpine/curios/man/vehicle_c.png");
    for (ImageResize imageResize : ImageResize.values())
      map.put(imageResize, new ScalableImage(bufferedImage));
  }

  @Override
  public JComponent getContainer() {
    return new JComponent() {
      @Override
      protected void paintComponent(Graphics graphics) {
        int piy = 0;
        for (ImageResize imageResize : ImageResize.values()) {
          BufferedImage bufferedImage = map.get(imageResize).getScaledInstance(imageResize, magnify);
          graphics.drawImage(bufferedImage, 0, piy, null);
          piy += bufferedImage.getHeight();
        }
      }
    };
  }

  static void main() {
    new ImageResizeDemo().runStandalone();
  }
}
