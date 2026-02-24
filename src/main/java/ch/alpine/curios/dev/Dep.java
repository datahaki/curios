// code by jph
package ch.alpine.curios.dev;

enum Dep {
  JUNIT_ENGINE("org.junit.jupiter", "junit-jupiter-engine", "6.0.3"),
  JUNIT_PARAMS("org.junit.jupiter", "junit-jupiter-params", "6.0.3"),
  COMPILER("org.apache.maven.plugins", "maven-compiler-plugin", "3.15.0"),
  SOURCE("org.apache.maven.plugins", "maven-source-plugin", "3.4.0"),
  JAVADOC("org.apache.maven.plugins", "maven-javadoc-plugin", "3.12.0"),
  SUREFIRE("org.apache.maven.plugins", "maven-surefire-plugin", "3.5.5"),
  FAILSAFE("org.apache.maven.plugins", "maven-failsafe-plugin", "3.5.5"),
  DEPLOY("org.apache.maven.plugins", "maven-deploy-plugin", "3.1.4"),
  TENSOR("ch.alpine", "tensor", "1.0.7"),
  FLATLAF("com.formdev", "flatlaf", "3.7"),
  BRIDGE("ch.alpine", "bridge", "0.3.6"),
  SUBARE("ch.alpine", "subare", "0.4.4"),
  SOPHUS("ch.alpine", "sophus", "0.1.0"),
  SOPHIS("ch.alpine", "sophis", "0.0.1"),
  ASCONY("ch.alpine", "ascony", "0.0.1"),
  //
  ;

  private final String groupId;
  private final String artifactId;
  private final String version;

  public boolean matchGroupId(String line) {
    return line.trim().equals(groupId);
  }

  public boolean matchArtifactId(String line) {
    return line.trim().equals(artifactId);
  }

  public boolean containsVersion(String line) {
    return line.contains("<version>") //
        && line.contains("</version>");
  }

  public boolean matchesVersion(String line) {
    return line.trim().equals(version);
  }

  Dep(String groupId, String artifactId, String version) {
    this.groupId = "<groupId>" + groupId + "</groupId>";
    this.artifactId = "<artifactId>" + artifactId + "</artifactId>";
    this.version = "<version>" + version + "</version>";
  }

  String version() {
    return version;
  }
}
