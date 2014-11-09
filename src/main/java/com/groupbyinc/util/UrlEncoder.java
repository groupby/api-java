package com.groupbyinc.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlEncoder {

    public static String encode(String pToEncode) {
        try {
            return URLEncoder.encode(pToEncode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(
                    "UTF 8 does not seem to be supported on this JVM");
        }
    }

    public static String decode(String pToEncode) {
        try {
            return URLDecoder.decode(pToEncode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(
                    "UTF 8 does not seem to be supported on this JVM");
        }
    }

}
