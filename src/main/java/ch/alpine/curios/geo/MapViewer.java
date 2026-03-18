// code by jph
package ch.alpine.curios.geo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import ch.alpine.bridge.awt.AwtUtil;
import ch.alpine.bridge.fig.Ticks;
import ch.alpine.bridge.geo.GeoComponent;
import ch.alpine.bridge.geo.TilePixel;
import ch.alpine.bridge.geo.TileServers;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Partition;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.io.Import;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.UnitConvert;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

@ReflectionMarker
class MapViewer implements ManipulateProvider {
  public TileServers tileServers = TileServers.OpenStreetMap;
  public Color marker = Color.MAGENTA;
  public Boolean crosshair = true;
  public Boolean gridlines = true;
  public Boolean showCycles = false;
  private final GeoComponent geoComponent = new GeoComponent() {
    @Override
    public void renderMore(Graphics2D graphics) {
      Dimension dimension = getSize();
      final Point center = AwtUtil.center(dimension);
      /* upper left corner */
      final TilePixel origin = tilePixel.shift(-center.x, -center.y);
      if (crosshair) { // draw crosshair
        graphics.setStroke(new BasicStroke());
        graphics.setColor(new Color(255, 0, 0, 192));
        int r = 3;
        graphics.drawLine(center.x - r, center.y, center.x + r, center.y);
        graphics.drawLine(center.x, center.y - r, center.x, center.y + r);
        graphics.setColor(Color.BLACK);
        graphics.drawString("z=" + tilePixel.tile().z(), 0 + 1, 20 + 1);
        graphics.setColor(Color.WHITE);
        graphics.drawString("z=" + tilePixel.tile().z(), 0, 20);
      }
      if (gridlines && 2 < tilePixel.tile().z()) {
        ScalarUnaryOperator suo = UnitConvert.SI().to("deg");
        { // lat
          Scalar max = suo.apply(tilePixel.shift(0, -center.y).lat_lon().Get(0));
          Scalar min = suo.apply(tilePixel.shift(0, +center.y).lat_lon().Get(0));
          while (Scalars.lessThan(max, min))
            max = max.add(Quantity.of(180, "deg"));
          Clip clip = Clips.interval(min, max);
          List<Scalar> list = Ticks.stream(clip, Rational.of(100, dimension.width)).toList();
          Tensor lat_lon = tilePixel.lat_lon().maps(suo);
          for (Scalar tick : list) {
            lat_lon.set(tick, 0);
            TilePixel from = tilePixel.from(lat_lon);
            graphics.setColor(new Color(255, 255, 255, 128));
            int x = (int) (from.absx() - origin.absx());
            int y = (int) (from.absy() - origin.absy());
            graphics.drawLine(x - 10, y, x + 10, y);
            graphics.drawString(" " + Ticks.format(tick), x, y);
          }
        }
        { // lon
          Scalar min = suo.apply(tilePixel.shift(-center.x, 0).lat_lon().Get(1));
          Scalar max = suo.apply(tilePixel.shift(+center.x, 0).lat_lon().Get(1));
          while (Scalars.lessThan(max, min))
            max = max.add(Quantity.of(360, "deg"));
          Clip clip = Clips.interval(min, max);
          List<Scalar> list = Ticks.stream(clip, Rational.of(100, dimension.width)).toList();
          Tensor lat_lon = tilePixel.lat_lon().maps(suo);
          for (Scalar tick : list) {
            lat_lon.set(tick, 1);
            TilePixel from = tilePixel.from(lat_lon);
            graphics.setColor(new Color(255, 255, 255, 128));
            int x = (int) (from.absx() - origin.absx());
            int y = (int) (from.absy() - origin.absy());
            graphics.drawLine(x, y - 10, x, y + 10);
            graphics.drawString(" " + Ticks.format(tick), x, y);
          }
        }
      }
      if (showCycles) {
        graphics.setColor(marker);
        graphics.setStroke(new BasicStroke(4f));
        for (Tensor seg : segments) {
          TilePixel beg = tilePixel.from(seg.get(0));
          TilePixel end = tilePixel.from(seg.get(1));
          int p1x = (int) (beg.absx() - origin.absx());
          int p1y = (int) (beg.absy() - origin.absy());
          int p2x = (int) (end.absx() - origin.absx());
          int p2y = (int) (end.absy() - origin.absy());
          graphics.drawLine(p1x, p1y, p2x, p2y);
        }
      }
    };
  };
  private TilePixel tilePixel;
  private final Tensor segments = segments();

  public MapViewer() {
    tilePixel = TilePixel.from(7, Quantity.of(38.343373, "deg"), Quantity.of(-0.762800, "deg"));
    geoComponent.tilePixel = tilePixel;
  }

  @Override
  public Container getContainer() {
    geoComponent.tileServers = tileServers;
    geoComponent.getCache().debug_print = true;
    return geoComponent;
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
