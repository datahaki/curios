// code by jph
package ch.alpine.curios.img;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import ch.alpine.tensor.img.ImageRotate;

public enum PhotoImport {
  ;
  public static BufferedImage of(Path file) throws IOException {
    final boolean rotate = PhotoImport.rotation(file);
    System.out.println("rotation = " + rotate);
    BufferedImage result = ImageIO.read(file.toFile());
    if (rotate)
      result = ImageRotate.cw(result);
    return result;
  }

  public static boolean rotation(Path file) {
    try {
      Metadata metadata = ImageMetadataReader.readMetadata(file.toFile());
      for (Directory d : metadata.getDirectories())
        if (d.getName().equals("Exif IFD0")) {
          System.out.println(d);
          for (Tag tag : d.getTags())
            if (tag.getTagName().equals("Orientation")) {
              String description = tag.getDescription();
              System.out.println(description);
              // Top, left side (Horizontal / normal)
              return description.equals("Right side, top (Rotate 90 CW)");
            }
        }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return false;
  }
}
