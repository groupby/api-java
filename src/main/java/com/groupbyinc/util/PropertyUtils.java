package com.groupbyinc.util;

/**
 * Created by osman on 18/09/14.
 */
public class PropertyUtils {
    public static boolean getFlag(String pName) {
        String prop = System.getProperty(pName);
        return "".equals(prop) || Boolean.parseBoolean(prop);
    }
}
