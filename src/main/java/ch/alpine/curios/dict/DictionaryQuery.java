// code by jph
package ch.alpine.curios.dict;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
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
import ch.alpine.bridge.ref.ann.FieldSelectionCallback;
import ch.alpine.bridge.ref.ann.FieldSlider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ext.EditDistance;
import ch.alpine.tensor.ext.HomeDirectory;

@ReflectionMarker
class DictionaryQuery implements ManipulateProvider {
  @FieldSelectionCallback("names")
  public String section = "eng-";

  public List<String> names() {
    return dictCollection.filters();
  }

  public String search = "";
  public String draft = "";
  @FieldClip(min = "0", max = "100")
  public Integer limit = 3;
  public Font font = new Font(Font.DIALOG, Font.PLAIN, 14);
  @FieldClip(min = "1", max = "5")
  @FieldSlider(showValue = true)
  public Integer d_max = 2;
  @FieldFuse
  public transient Boolean close = false;
  @FieldFuse
  public transient Boolean info = false;
  // ---
  private final JScrollPane jScrollPane;
  private final JTextPane jTextPane = new JTextPane();
  private final StyledDocument styledDocument = jTextPane.getStyledDocument();
  private final DictCollection dictCollection;

  public DictionaryQuery(DictCollection dictCollection) {
    this.dictCollection = dictCollection;
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
      for (Dict dict : dictCollection.filter(section).toList()) {
        for (String entry : dict.keySet()) {
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
    } else //
    if (info) {
      String collect = dictCollection.filter("").map(dict -> dict.toString()).collect(Collectors.joining("\n"));
      try {
        styledDocument.insertString(styledDocument.getLength(), collect, null);
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
      info = false;
    } else {
      if (dictCollection.filter(section).count() == 0) {
        try {
          styledDocument.insertString(0, "empty", null);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        // TODO identify by color or so, where the results are from
        List<String> list = new LinkedList<>();
        dictCollection.filter(section).forEach(d -> d.lookup(search).forEach(list::add));
        dictCollection.filter(section).forEach(d -> d.findIn(search).limit(limit).forEach(list::add));
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

  static void main() {
    if (FileBlock.of(ResourceLocator.of(DictionaryQuery.class).resolve("")))
      return;
    FieldsEditorParam.GLOBAL.textFieldFont_override = true;
    FieldsEditorParam.GLOBAL.textFieldFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
    // ---
    Path root = HomeDirectory.Database.resolve("freedict");
    new DictionaryQuery(DictDiscovery.of(root)).runStandalone();
  }
}
