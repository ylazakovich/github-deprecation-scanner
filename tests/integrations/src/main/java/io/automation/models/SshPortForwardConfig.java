package io.automation.models;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Builder
@Accessors(chain = true)
public class SshPortForwardConfig {

  /**
   * Remote host ip address for connecting via ssh.
   */
  private String hostIp;
  /**
   * Local host ip address for port forwarding.
   */
  private String localHostIp;
  /**
   * Username for connecting via ssh.
   */
  private String userName;
  /**
   * Path to local private key for connecting via ssh.
   */
  private String privateKeyPath;
  /**
   * Private key passphrase for connecting via ssh.
   */
  private String privateKeyPassphrase;
  /**
   * Remote host port for connecting via ssh.
   */
  private int hostPort;
  /**
   * Local port for port forwarding.
   */
  private int localPort;
  /**
   * Remote port for port forwarding.
   */
  private int remotePort;
}
