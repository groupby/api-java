package com.groupbyinc.api.config;

import com.groupbyinc.common.apache.http.client.config.RequestConfig;

public class ConnectionConfiguration {

  /**
   * See {@link RequestConfig#getConnectTimeout() connectTimeout}
   */
  public static final int DEFAULT_CONNECT_TIMEOUT = 1500;
  /**
   * See {@link RequestConfig#getConnectionRequestTimeout() connectionRequestTimeout}
   */
  public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 1500;
  /**
   * See {@link RequestConfig#getSocketTimeout() socketTimeout}
   */
  public static final int DEFAULT_SOCKET_TIMEOUT = 3000;
  /**
   * The maximum number of connections that will be opened by the connection pool
   */
  public static final int DEFAULT_MAX_CONNECTIONS = 200;
  /**
   * The maximum number of connections per route that will be opened by the connection pool
   */
  public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 100;

  private int connectTimeout;
  private int connectionRequestTimeout;
  private int socketTimeout;

  private int maxConnections;
  private int maxConnectionsPerRoute;

  public ConnectionConfiguration() {
    this(DEFAULT_CONNECT_TIMEOUT, DEFAULT_CONNECTION_REQUEST_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
  }

  public ConnectionConfiguration(int connectTimeout, int connectionRequestTimeout, int socketTimeout) {
    this(connectTimeout, connectionRequestTimeout, socketTimeout, DEFAULT_MAX_CONNECTIONS, DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
  }

  public ConnectionConfiguration(int connectTimeout, int connectionRequestTimeout, int socketTimeout, int maxConnections, int maxConnectionsPerRoute) {
    this.connectTimeout = connectTimeout;
    this.connectionRequestTimeout = connectionRequestTimeout;
    this.socketTimeout = socketTimeout;

    checkMaxConnections(maxConnections);
    checkMaxConnectionsPerRoute(maxConnectionsPerRoute);
    this.maxConnections = maxConnections;
    this.maxConnectionsPerRoute = maxConnectionsPerRoute;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public ConnectionConfiguration setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public int getConnectionRequestTimeout() {
    return connectionRequestTimeout;
  }

  public ConnectionConfiguration setConnectionRequestTimeout(int connectionRequestTimeout) {
    this.connectionRequestTimeout = connectionRequestTimeout;
    return this;
  }

  public int getSocketTimeout() {
    return socketTimeout;
  }

  public ConnectionConfiguration setSocketTimeout(int socketTimeout) {
    this.socketTimeout = socketTimeout;
    return this;
  }

  public int getMaxConnections() {
    return maxConnections;
  }

  public ConnectionConfiguration setMaxConnections(int maxConnections) {
    checkMaxConnections(maxConnections);
    this.maxConnections = maxConnections;
    return this;
  }

  public int getMaxConnectionsPerRoute() {
    return maxConnectionsPerRoute;
  }

  public ConnectionConfiguration setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
    checkMaxConnectionsPerRoute(maxConnectionsPerRoute);
    this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    return this;
  }

  private void checkMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
    if (maxConnectionsPerRoute > DEFAULT_MAX_CONNECTIONS_PER_ROUTE) {
      throw new IllegalStateException("Max active connections per route set above limit of " + DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
    }
  }

  private void checkMaxConnections(int maxConnections) {
    if (maxConnections > DEFAULT_MAX_CONNECTIONS) {
      throw new IllegalStateException("Max active connections set above limit of " + DEFAULT_MAX_CONNECTIONS);
    }
  }
}
