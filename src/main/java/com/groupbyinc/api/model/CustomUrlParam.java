package com.groupbyinc.api.model;

/**
 * <code>
 * Custom URL Parameters are used to trigger Rules based on any piece of information that
 * IT can pass through the engine.
 * For example, if you know that a user is signed in, and is a high net worth client,
 * then a Custom URL Parameter can be used to fire a rule exclusively for high net worth clients.
 * </code>
 *
 * @author will
 */
public class CustomUrlParam {

  private String key;
  private String value;

  /**
   * @return The name of this parameter.
   */
  public String getKey() {
    return key;
  }

  /**
   * @param key
   *         Set the name of this key
   *
   * @return
   */
  public CustomUrlParam setKey(String key) {
    this.key = key;
    return this;
  }

  /**
   * @return The value associated with this key.
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value
   *         Set the value.
   *
   * @return
   */
  public CustomUrlParam setValue(String value) {
    this.value = value;
    return this;
  }
}
