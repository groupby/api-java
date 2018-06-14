package com.groupbyinc.api.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionConfigurationTest {

  @Test
  public void testExceedMaxConnectionLimit() {
    try {
      new ConnectionConfiguration(0, 0, 0, 1000, 0);
    } catch (IllegalStateException e) {
      assertEquals("Max active connections set above limit of 200", e.getMessage());
    }

    try {
      new ConnectionConfiguration().setMaxConnections(1000);
    } catch (IllegalStateException e) {
      assertEquals("Max active connections set above limit of 200", e.getMessage());
    }
  }

  @Test
  public void testExceedMaxConnectionPerRouteLimit() {
    try {
      new ConnectionConfiguration(0, 0, 0, 0, 1000);
    } catch (IllegalStateException e) {
      assertEquals("Max active connections per route set above limit of 100", e.getMessage());
    }

    try {
      new ConnectionConfiguration().setMaxConnectionsPerRoute(1000);
    } catch (IllegalStateException e) {
      assertEquals("Max active connections per route set above limit of 100", e.getMessage());
    }
  }
}