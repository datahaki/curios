// code by jph
package ch.alpine.curios.man;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.img.CommonestFilter;
import ch.alpine.tensor.img.MaxFilter;
import ch.alpine.tensor.img.MeanFilter;
import ch.alpine.tensor.img.MedianFilter;
import ch.alpine.tensor.img.MinFilter;

public enum ImageFilters {
  COMMONEST {
    @Override
    Tensor filter(Tensor tensor, int n) {
      return CommonestFilter.of(tensor, n);
    }
  },
  MAX {
    @Override
    Tensor filter(Tensor tensor, int n) {
      return MaxFilter.of(tensor, n);
    }
  },
  MIN {
    @Override
    Tensor filter(Tensor tensor, int n) {
      return MinFilter.of(tensor, n);
    }
  },
  MEAN {
    @Override
    Tensor filter(Tensor tensor, int n) {
      return MeanFilter.of(tensor, n);
    }
  },
  MEDIAN {
    @Override
    Tensor filter(Tensor tensor, int n) {
      return MedianFilter.of(tensor, n);
    }
  };

  abstract Tensor filter(Tensor tensor, int n);
}
