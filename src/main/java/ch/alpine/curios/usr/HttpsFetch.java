// code by jph
package ch.alpine.curios.usr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

enum HttpsFetch {
  ;
  static void main() throws IOException, InterruptedException, URISyntaxException {
    // Create a new trust manager that trust all certificates
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
      @Override
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        System.out.println("ISSUERS");
        return null;
      }

      @Override
      public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        System.out.println("CLIENT");
      }

      @Override
      public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        System.out.println("SERVER");
      }
    } };
    // Activate the new trust manager
    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
      // ---
    }
    URL url = new URI( //
        "https://ec.europa.eu/eurostat/estat-navtree-portlet-prod/BulkDownloadListing?file=data/ert_bil_eur_d.tsv.gz").toURL();
    URLConnection connection = url.openConnection();
    try (InputStream is = connection.getInputStream()) {
      Thread.sleep(Duration.ofSeconds(1));
      System.out.println(is.available());
      byte[] data = is.readAllBytes();
      System.out.println(new String(data));
    }
  }
}
