// code by jph
package ch.alpine.curios.geo;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import javax.swing.JComponent;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;

@ReflectionMarker
class MapView implements ManipulateProvider {
  static final int N = 5;
  static final int M = 4;
  public Integer z = 3;
  public Integer x = 0;
  public Integer y = 0;
  private final JComponent jComponent = new JComponent() {
    @Override
    protected void paintComponent(Graphics graphics) {
      try {
        for (int i = 0; i < N; ++i)
          for (int j = 0; j < M; ++j)
            graphics.drawImage(UrlPathCache.get(tile.add(i, j)), 256 * i, 256 * j, null);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  };
  private Tile tile;

  public MapView() {
    tile = new Tile(z, x, y);
    jComponent.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        Point point = e.getPoint();
        if (point.x < 256 * N && point.y < 256 * M) {
          point.x /= 128;
          point.y /= 128;
          if (e.getWheelRotation() == 1)
            tile = tile.zoomIn(point.x, point.y);
          else
            tile = tile.zoomOut();
          MapView.this.x = tile.x();
          MapView.this.y = tile.y();
          MapView.this.z = tile.z();
          jComponent.repaint();
        }
      }
    });
    jComponent.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Dimension dimension = jComponent.getSize();
        int sx = dimension.width / 3;
        int sy = dimension.height / 3;
        Point point = e.getPoint();
        if (point.x < sx)
          tile = tile.add(-1, 0);
        if (2 * sx < point.x)
          tile = tile.add(+1, 0);
        if (point.y < sy)
          tile = tile.add(0, -1);
        if (2 * sy < point.y)
          tile = tile.add(0, +1);
        jComponent.repaint();
      }
    });
  }

  @Override
  public Container getContainer() {
    return jComponent;
  }

  static void main() {
    new MapView().runStandalone();
  }
}
