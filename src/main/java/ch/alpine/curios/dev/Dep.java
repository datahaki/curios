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
  GPG("org.apache.maven.plugins", "maven-gpg-plugin", "3.2.8"),
  SONATYPE("org.sonatype.central", "central-publishing-maven-plugin", "0.10.0"),
  DEPLOY("org.apache.maven.plugins", "maven-deploy-plugin", "3.1.4"),
  FLATLAF("com.formdev", "flatlaf", "3.7.1"),
  PITEST("org.pitest", "pitest-maven", "1.22.1"),
  TENSOR("io.github.datahaki", "tensor", "1.1.0"),
  BRIDGE("io.github.datahaki", "bridge", "0.3.6"),
  SUBARE("io.github.datahaki", "subare", "0.4.4"),
  SOPHUS("io.github.datahaki", "sophus", "0.1.0"),
  SOPHIS("io.github.datahaki", "sophis", "0.0.1"),
  ASCONY("io.github.datahaki", "ascony", "0.0.1"),
  QHULL3("io.github.datahaki", "qhull3", "0.0.1"),
  OWLETS("io.github.datahaki", "owlets", "0.0.1"),
  //
  ;

  private final String groupId;
  private final String artifactId;
  private final String tagged_groupId;
  private final String tagged_artifactId;
  private final String tagged_version;

  public boolean matchGroupId(String line) {
    return line.trim().equals(tagged_groupId);
  }

  public boolean matchArtifactId(String line) {
    return line.trim().equals(tagged_artifactId);
  }

  public boolean containsVersion(String line) {
    return line.contains("<version>") //
        && line.contains("</version>");
  }

  public boolean matchesVersion(String line) {
    return line.trim().equals(tagged_version);
  }

  Dep(String groupId, String artifactId, String version) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    // ---
    tagged_groupId = "<groupId>" + groupId + "</groupId>";
    tagged_artifactId = "<artifactId>" + artifactId + "</artifactId>";
    tagged_version = "<version>" + version + "</version>";
  }

  String tagged_version() {
    return tagged_version;
  }

  public String website() {
    return "https://mvnrepository.com/artifact/" + groupId + "/" + artifactId;
  }

  String groupId() {
    return groupId;
  }
}
