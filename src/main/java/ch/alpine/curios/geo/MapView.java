// code by jph
package ch.alpine.curios.geo;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import ch.alpine.bridge.awt.AwtUtil;
import ch.alpine.bridge.fig.geo.TileServer;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.qty.Quantity;

@ReflectionMarker
class MapView implements ManipulateProvider {
  private final JComponent jComponent = new JComponent() {
    @Override
    protected void paintComponent(Graphics graphics) {
      Dimension dimension = jComponent.getSize();
      Point center = AwtUtil.center(dimension);
      TileCoordinate ol = tileCoordinate.shift(-center.x, -center.y);
      for (int ix = 0; ix < dimension.width + 256; ix += 256)
        for (int iy = 0; iy < dimension.height + 256; iy += 256) {
          TileCoordinate shift = ol.shift(ix, iy);
          graphics.drawImage(urlPathCache.getTile(shift.tile()), ix - shift.pix(), iy - shift.piy(), null);
        }
      graphics.setColor(Color.RED);
      graphics.drawLine(center.x - 2, center.y, center.x + 2, center.y);
      graphics.drawLine(center.x, center.y - 2, center.x, center.y + 2);
      graphics.setColor(Color.WHITE);
      graphics.drawString("z=" + tileCoordinate.tile().z(), 0, 20);
    }
  };
  private final UrlPathCache urlPathCache = new UrlPathCache(TileServer.OPENTOPOMAP);
  private TileCoordinate tileCoordinate;

  public MapView() {
    // ,
    TileCoordinate from = EarthCoordinate.from(8, Quantity.of(38.343373, "deg"), Quantity.of(-0.762800, "deg"));
    tileCoordinate = from; // new TileCoordinate(new Tile(4, 8, 6), 200, 100);
    jComponent.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        tileCoordinate = tileCoordinate.zoom(e.getWheelRotation());
        jComponent.repaint();
      }
    });
    MouseInputListener mouseInputListener = new MouseInputAdapter() {
      private Point down;

      @Override
      public void mousePressed(MouseEvent e) {
        down = e.getPoint();
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        Point here = e.getPoint();
        int dx = down.x - here.x;
        int dy = down.y - here.y;
        down = here;
        tileCoordinate = tileCoordinate.shift(dx, dy);
        jComponent.repaint();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        down = null;
      }
    };
    jComponent.addMouseListener(mouseInputListener);
    jComponent.addMouseMotionListener(mouseInputListener);
  }

  @Override
  public Container getContainer() {
    return jComponent;
  }

  static void main() {
    new MapView().runStandalone();
  }
}
