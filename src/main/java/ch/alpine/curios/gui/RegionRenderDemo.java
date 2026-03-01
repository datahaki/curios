package ch.alpine.curios.gui;

import java.awt.Window;

import ch.alpine.ascony.ren.BallRegionRender;
import ch.alpine.ascony.ren.GridRender;
import ch.alpine.ascony.win.GeometricComponent;
import ch.alpine.ascony.win.TimerFrame;
import ch.alpine.bridge.pro.WindowProvider;
import ch.alpine.sophis.reg.BallRegion;
import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.qty.Quantity;

public class RegionRenderDemo implements WindowProvider {
  TimerFrame timerFrame = new TimerFrame();

  @Override
  public Window getWindow() {
    BallRegion ballRegion = new BallRegion(Tensors.fromString("{2[m],3[m]}"), Quantity.of(1, "m"));
    BallRegionRender ballRegionRender = new BallRegionRender(ballRegion);
    GeometricComponent geometricComponent = timerFrame.geometricComponent;
    Tensor model2Pixel = geometricComponent.getModel2Pixel();
    model2Pixel.set(s -> Quantity.of((Scalar) s, "m"), 0, 2);
    model2Pixel.set(s -> Quantity.of((Scalar) s, "m"), 1, 2);
    model2Pixel.set(s -> Quantity.of((Scalar) s, "m^-1"), 2, 0);
    model2Pixel.set(s -> Quantity.of((Scalar) s, "m^-1"), 2, 1);
    Tensor axes = Se2Matrix.model2pixel(Quantity.of(1, "m^-1"));
    model2Pixel = axes.dot(model2Pixel);
    geometricComponent.setModel2Pixel(model2Pixel);
    timerFrame.geometricComponent.addRenderInterface(ballRegionRender);
    GridRender gridRender = new GridRender(timerFrame.geometricComponent.jComponent::getSize);
    timerFrame.geometricComponent.addRenderInterfaceBackground(gridRender);
    return timerFrame.jFrame;
  }

  static void main() {
    new RegionRenderDemo().runStandalone();
  }
}
