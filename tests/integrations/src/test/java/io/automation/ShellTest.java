package io.automation;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.testng.annotations.Test;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

public class ShellTest {

  @Test
  void simpleTest() throws IOException, InterruptedException, TimeoutException {
    var script = "/Users/ylazakovich/IdeaProjects/ga-deprecation-scanner/entrypoint.sh";
    var path = "/Users/ylazakovich/IdeaProjects/ga-deprecation-scanner/logs/sample.log";
    var command = script + " " + path;
    ProcessResult result = new ProcessExecutor()
        .command("bash", "-c", command)
        .readOutput(true)
        .exitValue(0)
        .execute();
    System.out.println(result.outputUTF8());
  }
}
