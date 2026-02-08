// code by jph
package ch.alpine.curios.sbf;

import java.io.IOException;
import java.util.List;

import ch.alpine.bridge.io.HtmlUtf8;

class SbfExport {
  static void main() throws IOException {
    HtmlUtf8 htmlUtf8 = HtmlUtf8.page(SbfParser.ROOT.resolve("reference.htm"));
    for (SbfType sbfType : SbfType.values()) {
      htmlUtf8.appendln("<h2>");
      htmlUtf8.appendln(sbfType);
      htmlUtf8.appendln("</h2>");
      List<SbfItem> sbfItems = SbfParser.get(sbfType);
      htmlUtf8.appendln("<table border=0>");
      for (SbfItem sbfItem : sbfItems) {
        htmlUtf8.appendln("<tr>");
        htmlUtf8.appendln("<td colspan=" + (sbfItem.withoutImages() ? 2 : 1) + ">");
        htmlUtf8.appendln(sbfItem.question);
        // htmlUtf8.appendln("<br/>");
        htmlUtf8.appendln("<i>" + sbfItem.answers.getFirst() + "</i>");
        htmlUtf8.appendln("<br/>");
        if (!sbfItem.withoutImages()) {
          htmlUtf8.appendln("<td>");
          String locale = sbfItem.gfx.getParent().getFileName() + "/" + sbfItem.gfx.getFileName();
          // System.out.println(locale);
          htmlUtf8.appendln("<img src='" + locale + "'/>");
        }
        htmlUtf8.appendln("</tr>");
      }
      htmlUtf8.appendln("</table>");
    }
    htmlUtf8.close();
  }
}
