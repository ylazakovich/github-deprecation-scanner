package io.automation.utils;

import java.io.IOException;

import com.jcabi.ssh.Shell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.automation.models.ConstantFormat;
import io.automation.models.SshPortForwardConfig;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Utils for ssh commands.
 */
@Log4j2
@UtilityClass
public class SshUtils {

  private static final String HOST_KEY_CHECKING_RULE = "StrictHostKeyChecking";

  /**
   * Get text matches in file.
   *
   * @param shell      command line interface
   * @param pathToFile path to file
   * @param textToFind text to find in file
   * @return result of command
   */
  public static String getTextMatchesInFile(Shell shell, String pathToFile, String textToFind) {
    final String command = "%s '%s' %s".formatted(
        ShellCommand.GREP.lowerCase(), textToFind, pathToFile);
    return executeCommand(shell, command);
  }

  /**
   * Get last N rows from file.
   *
   * @param shell               command line interface
   * @param pathToFile          path to file
   * @param lastRowsCountToFind last rows count to find in file
   * @return result of command
   */
  public static String getLastRowsFromFile(Shell shell, String pathToFile, int lastRowsCountToFind) {
    final String command = "%s -n %d %s".formatted(
        ShellCommand.TAIL.lowerCase(), lastRowsCountToFind, pathToFile);
    return executeCommand(shell, command);
  }

  /**
   * Get file content.
   *
   * @param shell      command line interface
   * @param pathToFile path to file
   * @return result of command
   */
  public String getFileContent(Shell shell, String pathToFile) {
    final String command = "%s %s".formatted(ShellCommand.CAT.lowerCase(), pathToFile);
    return executeCommand(shell, command);
  }

  /**
   * Execute shell command.
   *
   * @param shell   command line interface
   * @param command command to execute
   * @return result of executed command
   */
  public static String executeCommand(Shell shell, String command) {
    return executeNotSafeCommand(new Shell.Safe(shell), command);
  }

  /**
   * Execute not safe shell command.
   *
   * @param shell   command line interface
   * @param command command to execute
   * @return result of executed command
   */
  @SneakyThrows(IOException.class)
  private static String executeNotSafeCommand(Shell shell, String command) {
    return new Shell.Plain(shell).exec(command);
  }

  /**
   * Create ssh session.
   *
   * @param config ssh port forward configuration
   * @return ssh bridge session
   */
  @SneakyThrows(JSchException.class)
  public static Session createSession(SshPortForwardConfig config) {
    JSch jsch = new JSch();
    jsch.addIdentity(config.getPrivateKeyPath(), config.getPrivateKeyPassphrase());
    Session session = jsch.getSession(config.getUserName(), config.getHostIp(), config.getHostPort());
    session.setConfig(HOST_KEY_CHECKING_RULE, BooleanUtils.toStringYesNo(false));
    session.connect();
    return session;
  }

  /**
   * Close ssh session.
   *
   * @param session ssh bridge session
   */
  public static void closeSession(Session session) {
    session.disconnect();
  }

  /**
   * Set ssh port forward.
   *
   * @param session ssh bridge session
   * @param config  ssh port forward configuration
   */
  @SneakyThrows(JSchException.class)
  public static void setPortForwarding(Session session, SshPortForwardConfig config) {
    session.setPortForwardingL(config.getLocalPort(), config.getLocalHostIp(), config.getRemotePort());
  }

  /**
   * Delete ssh port forward.
   *
   * @param session ssh bridge session
   * @param port    ssh forwarding port
   */
  @SneakyThrows(JSchException.class)
  public static void deletePortForwarding(Session session, int port) {
    session.delPortForwardingL(port);
  }

  public enum ShellCommand implements ConstantFormat {
    TAIL, GREP, CAT;


    @Override
    public String formatValue() {
      return name();
    }
  }
}
