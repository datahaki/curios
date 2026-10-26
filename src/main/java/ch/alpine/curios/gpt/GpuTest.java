// code by chatgpt
package ch.alpine.curios.gpt;

import ai.djl.Device;
import ai.djl.engine.Engine;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;

public class GpuTest {
  public static void main(String[] args) {
    System.setProperty("PYTORCH_FLAVOR", "cu121");
    System.setProperty("ai.djl.pytorch.num_interop_threads", "4");
    System.setProperty("ai.djl.pytorch.num_threads", "8");
    System.out.println("Engine: " + Engine.getInstance().getEngineName());
    System.out.println("Available devices: " + Engine.getInstance().getDevices());
    try (NDManager manager = NDManager.newBaseManager(Device.gpu(0))) {
      NDArray matrixA = manager.create(new float[] { 1, 2, 3, 4 }, new Shape(2, 2));
      NDArray matrixB = manager.create(new float[] { 5, 6, 7, 8 }, new Shape(2, 2));
      System.out.println("Matrix A device: " + matrixA.getDevice());
      System.out.println("Matrix B device: " + matrixB.getDevice());
      NDArray result = matrixA.matMul(matrixB);
      System.out.println("Result device: " + result.getDevice());
      // System.out.println("Result: " + result.toDebugString(100, 100, 100, 100));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
