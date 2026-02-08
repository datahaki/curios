// code by jph
package ch.alpine.curios.sbf;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class SbfItem {
  public String question;
  public Path gfx;
  public final List<String> answers = new ArrayList<>();

  boolean withoutImages() {
    return Objects.isNull(gfx);
  }
}
