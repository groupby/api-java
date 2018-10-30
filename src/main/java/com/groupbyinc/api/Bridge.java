package com.groupbyinc.api;

import com.groupbyinc.api.config.ConnectionConfiguration;

/**
 * @internal
 */
public class Bridge extends AbstractBridge {

  public Bridge(String clientKey, String bridgeHost, int bridgePort) {
    this(clientKey, bridgeHost, bridgePort, false);
  }

  public Bridge(String clientKey, String bridgeHost, int bridgePort, boolean secure) {
    this(clientKey, bridgeHost, bridgePort, secure, new ConnectionConfiguration());
  }

  public Bridge(String clientKey, String bridgeHost, int bridgePort, boolean secure, ConnectionConfiguration configuration) {
    this(clientKey, (secure ? HTTPS : HTTP) + bridgeHost + COLON + bridgePort, configuration);
  }

  protected Bridge(String clientKey, String baseUrl) {
    this(clientKey, baseUrl, new ConnectionConfiguration());
  }

  protected Bridge(String clientKey, String baseUrl, ConnectionConfiguration configuration) {
    super(clientKey, baseUrl, configuration);
  }
}
