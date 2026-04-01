// code by jph
package ch.alpine.curios.dict;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
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

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.FieldsEditorParam;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ext.HomeDirectory;

@ReflectionMarker
class DictionaryQuery implements ManipulateProvider {
  public String search = "";
  public String draft = "";
  public Font font = new Font(Font.DIALOG, Font.PLAIN, 14);
  // ---
  private final JScrollPane jScrollPane;
  private final JTextPane jTextPane = new JTextPane();
  private final StyledDocument styledDocument = jTextPane.getStyledDocument();
  private final List<Dict> dicts;

  public DictionaryQuery(Dict... dict) {
    this.dicts = List.of(dict);
    jTextPane.setEditable(false);
    jScrollPane = new JScrollPane(jTextPane, //
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, //
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
  }

  @Override
  public Container getContainer() {
    List<String> list = dicts.stream().flatMap(d -> d.answer(search, 3).stream()).toList();
    jTextPane.setFont(font);
    try {
      styledDocument.remove(0, styledDocument.getLength());
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
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
    jTextPane.setCaretPosition(0);
    // JTextPane textPane = new JTextPane();
    // StyledDocument doc = textPane.getStyledDocument();
    // // Normal paragraph style
    // Style normal = doc.addStyle("normal", null);
    // StyleConstants.setFontSize(normal, 16);
    // // Paragraph style with spacing
    // Style spaced = doc.addStyle("spaced", normal);
    // StyleConstants.setSpaceAbove(spaced, 10f); // space before paragraph
    // StyleConstants.setSpaceBelow(spaced, 20f); // space after paragraph
    // try {
    // doc.insertString(doc.getLength(),
    // "First paragraph (no extra spacing)\n", normal);
    // doc.insertString(doc.getLength(),
    // "Second paragraph (with spacing)\n", spaced);
    // doc.insertString(doc.getLength(),
    // "Third paragraph (also spaced)\n", spaced);
    // } catch (BadLocationException e) {
    // e.printStackTrace();
    // }
    // int pos = 0;
    // while (matcher.find()) {
    // handleText(input, pos, matcher.start());
    // handleMatch(matcher);
    // pos = matcher.end();
    // }
    // handleText(input, pos, input.length());
    JViewport jViewport = jScrollPane.getViewport();
    jViewport.setViewPosition(new Point(0, 0));
    // chatgpt:
    // "Whenever the contents of a JScrollPane change size
    // then call revalidate() on the viewport view."
    jViewport.revalidate();
    return jScrollPane;
  }

  static void main() throws IOException {
    FieldsEditorParam.GLOBAL.textFieldFont_override = true;
    FieldsEditorParam.GLOBAL.textFieldFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
    // ---
    Path root = HomeDirectory.Database.resolve("freedict");
    Dict dictES = Dict.of(root.resolve("freedict-eng-spa-2025.11.23.dictd", "eng-spa", "eng-spa.index"));
    Dict dictSE = Dict.of(root.resolve("freedict-spa-eng-0.3.1.dictd", "spa-eng", "spa-eng.index"));
    Dict dictDS = Dict.of(root.resolve("freedict-deu-spa-2025.11.23.dictd", "deu-spa", "deu-spa.index"));
    Dict dictSD = Dict.of(root.resolve("freedict-spa-deu-0.1.dictd", "spa-deu", "spa-deu.index"));
    new DictionaryQuery(dictES, dictSE, dictDS, dictSD).runStandalone();
  }
}
