// code by jph
package ch.alpine.curios.mvn;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugins.surefire.report.ReportTestSuite;
import org.apache.maven.plugins.surefire.report.SurefireReportParser;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class MavenInvokerTestExecutor {
  public void runTests(Path projectRoot) {
    File pomFile = projectRoot.resolve("pom.xml").toFile();
    if (!pomFile.exists()) {
      throw new IllegalArgumentException("pom.xml not found at: " + pomFile.getAbsolutePath());
    }
    // 1. Configure the Invocation Request
    InvocationRequest request = new DefaultInvocationRequest();
    request.setPomFile(pomFile);
    // REPLACEMENT FOR setGoals: Use addArg instead
    request.addArg("test");
    // If you need to run multiple goals/arguments sequentially, chain them or use addArgs:
    // request.addArgs(List.of("clean", "test"));
    request.setBatchMode(true);
    StringBuilder consoleOutput = new StringBuilder();
    InvocationOutputHandler outputHandler = line -> {
      consoleOutput.append(line).append(System.lineSeparator());
    };
    request.setOutputHandler(outputHandler);
    request.setErrorHandler(outputHandler);
    Invoker invoker = new DefaultInvoker();
    try {
      InvocationResult result = invoker.execute(request);
      boolean success = (result.getExitCode() == 0);
      if (result.getExecutionException() != null) {
        throw new RuntimeException("Maven execution failed to start", result.getExecutionException());
      }
      // return new ExecutionResult(result.getExitCode(), consoleOutput.toString(), success);
    } catch (MavenInvocationException e) {
      throw new RuntimeException("Failed to invoke Maven plugins", e);
    }
    //
    // 2. Point to the surefire-reports directory
    File reportsDir = new File(projectRoot.toFile(), "target/surefire-reports");
    List<ReportTestSuite> testSuites = List.of();
    if (reportsDir.exists() && reportsDir.isDirectory()) {
      // SurefireReportParser handles parsing all XML reports in the directory natively
      SurefireReportParser parser = new SurefireReportParser(List.of(reportsDir), DummyCL.INSTANCE);
      try {
        testSuites = parser.parseXMLReportFiles();
      } catch (Exception e) {
        System.err.println("Failed to parse Surefire XML reports: " + e.getMessage());
      }
    }
    // // 3. Tabularize and write results into a CSV file
    // try (FileWriter writer = new FileWriter(csvOutputFile)) {
    // // Write CSV Headers
    // writer.append("TestSuite,TestName,TimeSeconds,Status,Details\n");
    // for (ReportTestSuite suite : testSuites) {
    // for (ReportTestCase testCase : suite.getTestCases()) {
    // String status = "SUCCESS";
    // String details = "";
    // if (testCase.hasError()) {
    // status = "ERROR";
    // details = escapeCsv(testCase.getFailure());
    // } else if (testCase.hasFailure()) {
    // status = "FAILURE";
    // details = escapeCsv(testCase.getFailure());
    // } else if (testCase.hasSkipped()) {
    // status = "SKIPPED";
    // details = escapeCsv(testCase.getSkipped());
    // }
    // writer.append(escapeCsv(suite.getName())).append(',').append(escapeCsv(testCase.getName())).append(',').append(String.valueOf(testCase.getTime()))
    // .append(',').append(status).append(',').append(details).append('\n');
    // }
    // }
    // }
    // System.out.println("Test results successfully exported to: " + csvOutputFile.getAbsolutePath());
    // System.out.println("Test results successfully exported to: " + csvOutputFile.getAbsolutePath());
  }

  public record ExecutionResult(int exitCode, String consoleOutput, boolean isSuccess) {
  }

  static void main() {
    Path path = Path.of("/home/datahaki/Projects/tensor");
    MavenInvokerTestExecutor mavenInvokerTestExecutor = new MavenInvokerTestExecutor();
    mavenInvokerTestExecutor.runTests(path);
  }
}
