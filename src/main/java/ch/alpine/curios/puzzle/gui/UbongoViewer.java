// code by jph
package ch.alpine.curios.puzzle.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.curios.puzzle.UbongoPublish;

@ReflectionMarker
class UbongoViewer implements ManipulateProvider {
  private static final int SCALE = 46;
  public UbongoPublish ubongoPublish = UbongoPublish.LETTERH1;

  @Override
  public Container getContainer() {
    return new JComponent() {
      @Override
      protected void paintComponent(Graphics graphics) {
        StaticHelper.draw((Graphics2D) graphics, ubongoPublish, SCALE);
      }
    };
  }

  static void main() {
    new UbongoViewer().runStandalone();
  }
}
