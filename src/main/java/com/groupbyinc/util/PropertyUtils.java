package com.groupbyinc.util;

/**
 * Created by osman on 18/09/14.
 */
public class PropertyUtils {

  public static boolean getFlag(String name) {
    String prop = System.getProperty(name);
    return "".equals(prop) || Boolean.parseBoolean(prop);
  }
}
