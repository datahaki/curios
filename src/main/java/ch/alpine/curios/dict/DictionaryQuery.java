// code by jph
package ch.alpine.curios.dict;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ch.alpine.bridge.io.FileBlock;
import ch.alpine.bridge.io.ResourceLocator;
import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.FieldsEditorParam;
import ch.alpine.bridge.ref.ann.FieldClip;
import ch.alpine.bridge.ref.ann.FieldFuse;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ext.EditDistance;
import ch.alpine.tensor.ext.HomeDirectory;

@ReflectionMarker
class DictionaryQuery implements ManipulateProvider {
  public String search = "";
  public String draft = "";
  @FieldClip(min = "0", max = "100")
  public Integer limit = 3;
  public Font font = new Font(Font.DIALOG, Font.PLAIN, 14);
  @FieldClip(min = "1", max = "5")
  @FieldSlider(showValue = true)
  public Integer d_max = 2;
  @FieldFuse
  public Boolean close = false;
  // ---
  private final JScrollPane jScrollPane;
  private final JTextPane jTextPane = new JTextPane();
  private final StyledDocument styledDocument = jTextPane.getStyledDocument();
  private final List<Dict> dicts;

  public DictionaryQuery(Dict... dict) {
    this.dicts = List.of(dict);
    jTextPane.setEditable(false);
    jTextPane.setPreferredSize(new Dimension(400, 400));
    jScrollPane = new JScrollPane(jTextPane, //
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, //
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
  }

  @Override
  public Container getContainer() {
    jTextPane.setFont(font);
    try {
      styledDocument.remove(0, styledDocument.getLength());
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
    if (close) {
      // int total = 0;
      // Timing timing = Timing.started();
      for (Dict dict : dicts) {
        for (String entry : dict.map.keySet()) {
          int dist = EditDistance.of(search, entry);
          // ++total;
          if (dist <= d_max) {
            try {
              styledDocument.insertString(styledDocument.getLength(), entry + "\n", null);
            } catch (BadLocationException e) {
              e.printStackTrace();
            }
          }
        }
      }
      // IO.println(timing.seconds() + " " + total);
      close = false;
    } else {
      List<String> list = new LinkedList<>();
      dicts.stream().forEach(d -> d.lookup(search).forEach(list::add));
      dicts.stream().forEach(d -> d.findIn(search).limit(limit).forEach(list::add));
      Style base = styledDocument.addStyle("normal", null);
      StyleConstants.setFontFamily(base, font.getFamily());
      StyleConstants.setFontSize(base, font.getSize());
      Style bold = styledDocument.addStyle("bold", base);
      StyleConstants.setBold(bold, true);
      String string = list.stream().flatMap(String::lines).collect(Collectors.joining("\n"));
      MatchWrap matchWrap = new MatchWrap() {
        @Override
        public void handle(int beg, int end) {
          try {
            styledDocument.insertString(styledDocument.getLength(), string.substring(beg, end), base);
          } catch (BadLocationException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void handle(Matcher matcher) {
          try {
            styledDocument.insertString(styledDocument.getLength(), string.substring(matcher.start(), matcher.end()), bold);
          } catch (BadLocationException e) {
            e.printStackTrace();
          }
        }
      };
      Pattern pattern = Pattern.compile(search);
      Matcher matcher = pattern.matcher(string);
      StringMatcher.stream(matcher, matchWrap);
    }
    jTextPane.setCaretPosition(0);
    JViewport jViewport = jScrollPane.getViewport();
    jViewport.setViewPosition(new Point(0, 0));
    // chatgpt:
    // "Whenever the contents of a JScrollPane change size
    // then call revalidate() on the viewport view."
    jViewport.revalidate();
    return jScrollPane;
  }

  static void main() throws IOException {
    if (FileBlock.of(ResourceLocator.of(DictionaryQuery.class).resolve("")))
      return;
    FieldsEditorParam.GLOBAL.textFieldFont_override = true;
    FieldsEditorParam.GLOBAL.textFieldFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
    // ---
    Path root = HomeDirectory.Database.resolve("freedict");
    // 
    Dict dictES = Dict.of(root.resolve("freedict-eng-spa-2025.11.23.dictd", "eng-spa", "eng-spa.index"));
    Dict dictSE = Dict.of(root.resolve("freedict-spa-eng-0.3.1.dictd", "spa-eng", "spa-eng.index"));
//    Dict dictEC = Dict.of(root.resolve("freedict-eng-cat-2025.11.23.dictd", "eng-cat", "eng-cat.index"));
//    Dict dictCE = Dict.of(root.resolve("freedict-cat-eng-2025.11.23.dictd", "cat-eng", "cat-eng.index"));
    Dict dictDS = Dict.of(root.resolve("freedict-deu-spa-2025.11.23.dictd", "deu-spa", "deu-spa.index"));
    Dict dictSD = Dict.of(root.resolve("freedict-spa-deu-0.1.dictd", "spa-deu", "spa-deu.index"));
    Dict dictFS = Dict.of(root.resolve("freedict-fra-spa-2025.11.23.dictd", "fra-spa", "fra-spa.index"));
    Dict dictSF = Dict.of(root.resolve("freedict-spa-fra-2025.11.23.dictd", "spa-fra", "spa-fra.index"));
    new DictionaryQuery(dictES, dictSE, 
//        dictEC, dictCE, 
        dictDS, dictSD, dictFS, dictSF).runStandalone();
  }
}
