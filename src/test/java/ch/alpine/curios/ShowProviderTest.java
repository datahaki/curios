// code by jph
package ch.alpine.curios;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.pro.ShowProvider;
import ch.alpine.tensor.ext.ref.InstanceDiscovery;

class ShowProviderTest implements Consumer<ShowProvider> {
  @TestFactory
  Stream<DynamicTest> dynamicTests() {
    return InstanceDiscovery.of("ch.alpine", ShowProvider.class).stream() //
        .map(Supplier::get) //
        .map(instance -> DynamicTest.dynamicTest(instance.toString(), () -> accept(instance)));
  }

  @Override
  public void accept(ShowProvider showProvider) {
    Show show = showProvider.getShow();
    Dimension dimension = new Dimension(800, 800);
    BufferedImage bufferedImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = bufferedImage.createGraphics();
    show.render_autoIndent(graphics, new Rectangle(dimension));
    graphics.dispose();
  }
}
