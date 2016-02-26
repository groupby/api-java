package com.groupbyinc.api;

/**
 * @internal
 */
public class Bridge extends AbstractBridge {

  public Bridge(String clientKey, String bridgeHost, int bridgePort) {
    this(clientKey, bridgeHost, bridgePort, false);
  }

  public Bridge(String clientKey, String bridgeHost, int bridgePort, boolean secure) {
    this(clientKey, (secure ? HTTPS : HTTP) + bridgeHost + COLON + bridgePort);
  }

  protected Bridge(String clientKey, String baseUrl) {
    super(clientKey, baseUrl);
  }
}
