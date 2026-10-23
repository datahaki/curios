// code by jph
package ch.alpine.curios.img;

import java.awt.Container;
import java.io.File;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import ch.alpine.bridge.pro.ManipulateProvider;
import ch.alpine.bridge.ref.ann.ReflectionMarker;
import ch.alpine.tensor.ext.HomeDirectory;

@ReflectionMarker
class MetadataShow implements ManipulateProvider {
  private final JTextArea jTextArea = new JTextArea("");
  private final JScrollPane jScrollPane = new JScrollPane(jTextArea);
  public File path = HomeDirectory.Pictures.resolve("").toFile();

  @Override
  public Container getContainer() {
    StringBuilder stringBuilder = new StringBuilder();
    File file = path; // .toFile();
    if (file.isFile())
      try {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory directory : metadata.getDirectories()) {
          stringBuilder.append(directory + "\n");
          for (Tag tag : directory.getTags()) {
            stringBuilder.append("\u3000" + tag.getTagName() + "=" + tag.getDescription() + "\n");
          }
        }
      } catch (ImageProcessingException e) {
        e.printStackTrace();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    jTextArea.setText(stringBuilder.toString());
    return jScrollPane;
  }

  static void main() {
    new MetadataShow().runStandalone();
  }
}
