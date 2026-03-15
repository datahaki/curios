// code by jph
package ch.alpine.curios.geo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

class UrlPathCache {
  static final Path ROOT = HomeDirectory.Ephemeral.mk_dirs("opentopomap");
  // TODO use memory cache as well

  static BufferedImage get(Tile tile) throws IOException, InterruptedException {
    Path path = ROOT.resolve(tile.path());
    if (!Files.isRegularFile(path))
      download(tile, path);
    try {
      BufferedImage bufferedImage = ImageIO.read(path.toFile());
      return bufferedImage;
    } catch (Exception e) {
      IO.println(e);
      Files.deleteIfExists(path);
    }
    return null;
  }

  static void download(Tile tile, Path path) throws IOException, InterruptedException {
    String tileUrl = "https://tile.opentopomap.org/" + tile.z() + "/" + tile.x() + "/" + tile.y() + ".png";
    IO.println(tileUrl);
    HttpRequest httpRequest = HttpRequest.newBuilder() //
        .uri(URI.create(tileUrl)) //
        .header("User-Agent", "TileDownloader/1.0") //
        .GET().build();
    HttpResponse<byte[]> httpResponse = HttpClient.newHttpClient() //
        .send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
    Files.createDirectories(PathName.of(path).parent());
    Files.write(path, httpResponse.body());
  }

  static void main() throws IOException, InterruptedException {
    get(new Tile(3, 2, 4));
    get(new Tile(3, 2, 5));
    get(new Tile(3, 2, 6));
    get(new Tile(3, 2, 7));
  }
}
