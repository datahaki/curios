// code by jph
package ch.alpine.curios.sbf;

enum SbfType {
  basis("html", 1),
  see("html", 73),
  binnen("html", 73),
  segeln("html", 254),
  src("txt", 1),
  ubi("txt", 1);

  final String ext;
  final int ofs;

  private SbfType(String ext, int ofs) {
    this.ext = ext;
    this.ofs = ofs;
  }
}
