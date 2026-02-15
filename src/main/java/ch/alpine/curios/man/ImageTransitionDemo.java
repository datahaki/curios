// code by jph
package ch.alpine.curios.man;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.util.Objects;

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
public class ImageTransitionDemo implements ManipulateProvider {
  private final ScalableImage im1 = new ScalableImage(ResourceData.bufferedImage("/ch/alpine/curios/man/vehicle_c.png"));
  private final ScalableImage im2 = new ScalableImage(ResourceData.bufferedImage("/ch/alpine/curios/man/vehicle_g.png"));
  @FieldClip(min = "0.01", max = "0.1")
  @FieldSlider
  public Scalar ex = RealScalar.of(0.05);
  @FieldClip(min = "0", max = "1")
  @FieldSlider
  public Scalar c1 = RealScalar.of(0.3);

  @Override
  public JComponent getContainer() {
    return new JComponent() {
      @Override
      protected void paintComponent(Graphics g) {
        if (Objects.isNull(im1) || Objects.isNull(im2))
          return;
        Graphics2D graphics = (Graphics2D) g;
        Dimension dimension = getSize();
        Rectangle rectangle = new Rectangle(100, 50, dimension.width - 200, dimension.height - 100);
        // ---
        graphics.drawImage(im2.getScaledInstance(ImageResize.DEGREE_3, rectangle.width, rectangle.height), rectangle.x, rectangle.y, null);
        int ext = (int) (rectangle.width * ex.number().floatValue());
        int x = (int) ((rectangle.width + 2 * ext) * c1.number().floatValue()) - ext;
        int _x = Math.max(0, x);
        graphics.setClip(rectangle.x + _x, rectangle.y, rectangle.width - _x, rectangle.height);
        graphics.drawImage(im1.getScaledInstance(ImageResize.DEGREE_0, rectangle.width, rectangle.height), rectangle.x, rectangle.y, null);
        graphics.setClip(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        Color color_lo = new Color(255, 255, 255, 0);
        Color color_hi = new Color(0, 0, 0, 192);
        Paint paint = new LinearGradientPaint(rectangle.x + x - ext, 0, rectangle.x + x + ext, 0, new float[] { 0f, 0.5f, 1f },
            new Color[] { color_lo, color_hi, color_lo });
        graphics.setPaint(paint);
        graphics.fillRect(rectangle.x + x - ext, rectangle.y, 2 * ext, rectangle.height);
      }
    };
  }

  static void main() {
    new ImageTransitionDemo().run();
  }
}
