// code by jph
package ch.alpine.curios.dict;

import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.FieldsEditorParam;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ext.HomeDirectory;

@ReflectionMarker
class Spanish implements ManipulateProvider {
  public String search = "";
  public Font font = new Font(Font.DIALOG, Font.PLAIN, 14);
  // ---
  private final JScrollPane jScrollPane;
  private final JTextArea jTextArea = new JTextArea();
  private final List<Dict> dict;

  public Spanish(Dict... dict) {
    this.dict = List.of(dict);
    jTextArea.setLineWrap(true);
    jTextArea.setEditable(false);
    jScrollPane = new JScrollPane(jTextArea, //
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, //
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
  }

  @Override
  public Container getContainer() {
    List<String> list = dict.stream().flatMap(d -> d.lookup(search).stream()).toList();
    if (list.isEmpty())
      list = dict.get(0).findIn(search, 3);
    jTextArea.setText(list.stream().flatMap(String::lines).collect(Collectors.joining("\n")));
    jTextArea.setCaretPosition(0);
    jTextArea.setFont(font);
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
    new Spanish(dictES, dictSE, dictDS, dictSD).runStandalone();
  }
}
