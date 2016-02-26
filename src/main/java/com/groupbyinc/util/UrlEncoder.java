package com.groupbyinc.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlEncoder {

  public static String encode(String toEncode) {
    try {
      return URLEncoder.encode(toEncode, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("UTF 8 does not seem to be supported on this JVM");
    }
  }

  public static String decode(String toEncode) {
    try {
      return URLDecoder.decode(toEncode, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("UTF 8 does not seem to be supported on this JVM");
    }
  }
}
