// code by jph
package ch.alpine.curios.sbf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.ReadLine;

class SbfParser {
  public static final Path ROOT = HomeDirectory.Documents.resolve("sailmath", "resources");

  public static List<SbfItem> get(SbfType sbfType) throws IOException {
    switch (sbfType.ext) {
    case "html":
      return html(sbfType);
    case "txt":
      return text(sbfType);
    }
    throw new RuntimeException();
  }

  private static List<SbfItem> text(SbfType sbfType) throws IOException {
    Path file = ROOT.resolve(sbfType.name() + ".txt");
    List<String> lines = new ArrayList<>();
    try (InputStream inputStream = Files.newInputStream(file)) {
      lines = ReadLine.of(inputStream).toList();
    }
    int count = sbfType.ofs;
    List<SbfItem> sbfItems = new ArrayList<>();
    SbfItem sbfItem = null;
    for (String s : lines) {
      if (s.startsWith(count + ".")) {
        if (sbfItem != null) {
          int size = sbfItem.answers.size();
          if (size != 4) {
            System.out.println(s);
            System.out.println(size);
          }
        }
        sbfItem = new SbfItem();
        sbfItems.add(sbfItem);
        sbfItem.question = s;
        ++count;
      } else {
        sbfItem.answers.add(s.substring(s.indexOf(')') + 1).trim());
      }
    }
    return sbfItems;
  }

  private static List<SbfItem> html(SbfType sbfType) throws IOException {
    Path file = ROOT.resolve(sbfType.name() + ".html");
    Path folder = ROOT.resolve(sbfType.name());
    List<String> lines = new ArrayList<>();
    try (InputStream inputStream = Files.newInputStream(file)) {
      lines = ReadLine.of(inputStream).toList();
    }
    int count = sbfType.ofs;
    List<SbfItem> sbfItems = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    for (String s : lines) {
      if (s.startsWith(count + ".")) {
        stringBuilder = new StringBuilder();
        ++count;
      }
      if (s.equals("<p class=\"line\"></p>")) {
        SbfItem sbfItem = new SbfItem();
        String string = stringBuilder.toString();
        string = string.replaceAll("<br>", "");
        int prev = string.indexOf("<li>");
        String question = string.substring(0, prev);
        int pic = question.indexOf("<img src=");
        if (0 < pic) {
          int beg = question.indexOf('/', pic) + 1;
          int end = question.indexOf('"', beg);
          sbfItem.gfx = folder.resolve(question.substring(beg, end));
        }
        int last = question.indexOf("</p>");
        sbfItem.question = question.substring(0, last).trim();
        for (int c = 0; c < 4; ++c) {
          int next = string.indexOf("</li>", prev + 1);
          String answer = string.substring(prev, next).replaceAll("<li>", "").replaceAll("</li>", "");
          sbfItem.answers.add(answer);
          prev = next;
        }
        sbfItems.add(sbfItem);
      } else
        stringBuilder.append(s);
    }
    return sbfItems;
  }

  static void main() throws IOException {
    text(SbfType.src);
  }
}
