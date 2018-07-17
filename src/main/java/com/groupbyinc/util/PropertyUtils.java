package com.groupbyinc.util;

public class PropertyUtils {

  public static boolean getFlag(String name) {
    String prop = System.getProperty(name);
    return "".equals(prop) || Boolean.parseBoolean(prop);
  }
}
