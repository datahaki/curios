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

import ch.alpine.bridge.fig.geo.TileServer;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.ext.PathName;

class UrlPathCache {
  private final BufferedImage FALLBACK = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
  private final Cache<Tile, BufferedImage> cache = Cache.of(this::getSafe, 256);
  private final TileServer tileServer;
  private final Path ROOT;

  public UrlPathCache(TileServer tileServer) {
    this.tileServer = tileServer;
    ROOT = HomeDirectory.Ephemeral.mk_dirs(tileServer.name().toLowerCase());
  }

  public BufferedImage getTile(Tile tile) {
    return cache.apply(tile);
  }

  private BufferedImage getSafe(Tile tile) {
    try {
      return get(tile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return FALLBACK;
  }

  private BufferedImage get(Tile tile) throws IOException, InterruptedException {
    Path path = ROOT.resolve(tile.path());
    if (!Files.isRegularFile(path))
      download(tile, path);
    try {
      // IO.println("read=" + path);
      BufferedImage bufferedImage = ImageIO.read(path.toFile());
      return bufferedImage;
    } catch (Exception e) {
      IO.println(e);
      Files.deleteIfExists(path);
    }
    return null;
  }

  private void download(Tile tile, Path path) throws IOException, InterruptedException {
    URI uri = tileServer.uri(tile.z(), tile.x(), tile.y());
    IO.println("down=" + uri);
    HttpRequest httpRequest = HttpRequest.newBuilder() //
        .uri(uri) //
        .header("User-Agent", "TileDownloader/1.0") //
        .GET().build();
    HttpResponse<byte[]> httpResponse = HttpClient.newHttpClient() //
        .send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
    Files.createDirectories(PathName.of(path).parent());
    Files.write(path, httpResponse.body());
  }

  static void main() {
    UrlPathCache urlPathCache = new UrlPathCache(TileServer.OPENTOPOMAP);
    int z = 6;
    for (int iz = 0; iz <= z; ++iz) {
      int max = Tile.maxInclusive(iz);
      for (int ix = 0; ix <= max; ++ix)
        for (int iy = 0; iy <= max; ++iy) {
          Tile tile = new Tile(z, ix, iy);
          urlPathCache.getSafe(tile);
        }
    }
  }
}
