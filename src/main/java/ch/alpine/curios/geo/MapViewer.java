// code by jph
package ch.alpine.curios.geo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import ch.alpine.bridge.awt.AwtUtil;
import ch.alpine.bridge.geo.GeoComponent;
import ch.alpine.bridge.geo.TilePixel;
import ch.alpine.bridge.geo.TileServers;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Partition;
import ch.alpine.tensor.io.Import;
import ch.alpine.tensor.qty.Quantity;

@ReflectionMarker
class MapViewer implements ManipulateProvider {
  public Color marker = Color.MAGENTA;
  public TileServers tileServers = TileServers.OpenStreetMap;
  private final GeoComponent jComponent = new GeoComponent() {
    @Override
    public void renderMore(Graphics2D graphics) {
      Dimension dimension = getSize();
      Point center = AwtUtil.center(dimension);
      TilePixel origin = tilePixel.shift(-center.x, -center.y);
      int z = tilePixel.tile().z();
      graphics.setColor(marker);
      graphics.setStroke(new BasicStroke(4f));
      for (Tensor seg : segments) {
        TilePixel beg = TilePixel.from(z, seg.get(0));
        TilePixel end = TilePixel.from(z, seg.get(1));
        int p1x = (int) (beg.absx() - origin.absx());
        int p1y = (int) (beg.absy() - origin.absy());
        int p2x = (int) (end.absx() - origin.absx());
        int p2y = (int) (end.absy() - origin.absy());
        graphics.drawLine(p1x, p1y, p2x, p2y);
      }
    };
  };
  private TilePixel tilePixel;
  private final Tensor segments = segments();

  public MapViewer() {
    tilePixel = TilePixel.from(7, Quantity.of(38.343373, "deg"), Quantity.of(-0.762800, "deg"));
    jComponent.tilePixel = tilePixel;
  }

  @Override
  public Container getContainer() {
    jComponent.tileServers = tileServers;
    return jComponent;
  }

  static Tensor segments() {
    Tensor tensor = Import.of("ch/alpine/curios/geo/2024_routes.csv");
    return Tensor.of(tensor.stream() //
        .filter(r -> r.length() == 8) //
        .map(r -> Partition.of(r.extract(4, 8).maps(s -> Quantity.of(s, "deg")), 2)));
  }

  static void main() {
    new MapViewer().runStandalone();
  }
}
