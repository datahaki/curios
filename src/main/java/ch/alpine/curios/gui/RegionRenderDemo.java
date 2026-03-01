// code by jph
package ch.alpine.curios.gui;

import java.awt.Container;

import ch.alpine.ascony.ren.BallRegionRender;
import ch.alpine.ascony.ren.ConeRegionRender;
import ch.alpine.ascony.ren.EllipseRegionRender;
import ch.alpine.ascony.ren.GridRender;
import ch.alpine.ascony.ren.RenderInterface;
import ch.alpine.ascony.win.GeometricComponent;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.sophis.reg.BallRegion;
import ch.alpine.sophis.reg.ConeRegion;
import ch.alpine.sophis.reg.EllipsoidRegion;
import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.qty.Quantity;

@ReflectionMarker
public class RegionRenderDemo implements ManipulateProvider {
  private final GeometricComponent geometricComponent = new GeometricComponent();

  public RegionRenderDemo() {
    {
      Tensor model2Pixel = geometricComponent.getModel2Pixel();
      model2Pixel.set(s -> Quantity.of((Scalar) s, "m"), 0, 2);
      model2Pixel.set(s -> Quantity.of((Scalar) s, "m"), 1, 2);
      model2Pixel.set(s -> Quantity.of((Scalar) s, "m^-1"), 2, 0);
      model2Pixel.set(s -> Quantity.of((Scalar) s, "m^-1"), 2, 1);
      Tensor axes = Se2Matrix.model2pixel(Quantity.of(1, "m^-1"));
      model2Pixel = axes.dot(model2Pixel);
      geometricComponent.setModel2Pixel(model2Pixel);
    }
    {
      BallRegion ballRegion = new BallRegion(Tensors.fromString("{2[m],3[m]}"), Quantity.of(1, "m"));
      BallRegionRender ballRegionRender = new BallRegionRender(ballRegion);
      geometricComponent.addRenderInterface(ballRegionRender);
    }
    {
      ConeRegion coneRegion = new ConeRegion(Tensors.fromString("{-2[m],3[m],1.3}"), RealScalar.ONE);
      ConeRegionRender coneRegionRender = new ConeRegionRender(coneRegion);
      geometricComponent.addRenderInterface(coneRegionRender);
    }
    {
      EllipsoidRegion ellipsoidRegion = //
          new EllipsoidRegion(Tensors.fromString("{2[m],-1[m]}"), Tensors.fromString("{1[m],0.5[m]}"));
      RenderInterface renderInterface = EllipseRegionRender.of(ellipsoidRegion);
      geometricComponent.addRenderInterface(renderInterface);
    }
    {
      GridRender gridRender = new GridRender(geometricComponent.jComponent::getSize);
      geometricComponent.addRenderInterfaceBackground(gridRender);
    }
  }

  @Override
  public Container getContainer() {
    return geometricComponent.jComponent;
  }

  static void main() {
    new RegionRenderDemo().runStandalone();
  }
}
