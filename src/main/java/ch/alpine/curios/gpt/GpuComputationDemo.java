// code by jph
package ch.alpine.curios.gpt;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ch.alpine.bridge.pro.VoidProvider;

enum GpuComputationDemo implements VoidProvider {
  INSTANCE;

  @Override
  public Void runStandalone() {
    System.setProperty("PYTORCH_FLAVOR", "cu121");
    System.setProperty("ai.djl.pytorch.num_interop_threads", "4");
    System.setProperty("ai.djl.pytorch.num_threads", "8");
    // Create an NDManager assigned explicitly to your RTX 5060
    try (NDManager manager = NDManager.newBaseManager(Device.gpu())) {
      // Create two matrices directly on the GPU VRAM
      NDArray matrixA = manager.create(new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, new Shape(2, 2));
      NDArray matrixB = manager.create(new float[] { 5.0f, 6.0f, 7.0f, 8.0f }, new Shape(2, 2));
      // Perform matrix multiplication on the GPU
      NDArray result = matrixA.matMul(matrixB);
      // Print the result (pulls data from GPU back to CPU/RAM)
      System.out.println("Result: " + result);
    }
    return null;
  }

  static void main() {
    INSTANCE.runStandalone();
  }
}
