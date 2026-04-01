// code by jph
package ch.alpine.curios.dict;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

class StyledTextExample {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Styled Text with Colors");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(450, 300);
      JTextPane textPane = new JTextPane();
      textPane.setEditable(false);
      StyledDocument styledDocument = textPane.getStyledDocument();
      // Base style
      Style normal = styledDocument.addStyle("normal", null);
      StyleConstants.setFontFamily(normal, "SansSerif");
      StyleConstants.setFontSize(normal, 16);
      // Bold
      Style bold = styledDocument.addStyle("bold", normal);
      StyleConstants.setBold(bold, true);
      // Italic
      Style italic = styledDocument.addStyle("italic", normal);
      StyleConstants.setItalic(italic, true);
      // Red text
      Style red = styledDocument.addStyle("red", normal);
      StyleConstants.setForeground(red, Color.RED);
      // Blue + bold
      Style blueBold = styledDocument.addStyle("blueBold", normal);
      StyleConstants.setForeground(blueBold, Color.BLUE);
      StyleConstants.setBold(blueBold, true);
      try {
        styledDocument.insertString(styledDocument.getLength(), "This is ", normal);
        styledDocument.insertString(styledDocument.getLength(), "bold", bold);
        styledDocument.insertString(styledDocument.getLength(), ", this is ", normal);
        styledDocument.insertString(styledDocument.getLength(), "italic", italic);
        styledDocument.insertString(styledDocument.getLength(), ", this is ", normal);
        styledDocument.insertString(styledDocument.getLength(), "red", red);
        styledDocument.insertString(styledDocument.getLength(), ", and this is ", normal);
        styledDocument.insertString(styledDocument.getLength(), "blue & bold", blueBold);
        styledDocument.insertString(styledDocument.getLength(), ".", normal);
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
      frame.add(new JScrollPane(textPane));
      frame.setVisible(true);
    });
  }
}
