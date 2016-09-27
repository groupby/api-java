package com.groupbyinc.api.model;

import com.groupbyinc.common.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * <code>
 * A template is always returned. By default an empty template called
 * 'default' will be returned. If a rule is triggered the corresponding template
 * will be returned with the corresponding zones populated with the correct
 * content.
 * A template has the following properties
 *
 * - `id`: an MD5 of the name
 * - `name`: the name of this template.
 * - `zones`: a map of the zones by name.
 *
 * </code>
 *
 * @author will
 */
public class Template {

  @JsonProperty("_id") private String id;
  private String name;
  private String ruleName;
  private Map<String, Zone> zones = new HashMap<String, Zone>();

  /**
   * @return An MD5 hash of the name of this template.
   */
  public String getId() {
    return id;
  }

  /**
   * @param id
   *         Set the ID.
   *
   * @return
   */
  public Template setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * @return The name as set in the command center.
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *         Set the name
   *
   * @return
   */
  public Template setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @return The name of the rule that triggered this template.
   */
  public String getRuleName() {
    return ruleName;
  }

  /**
   * @param ruleName
   *         Set the rule.
   */
  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  /**
   * @return A map of zones keyed by name for easy lookup in the UI layer.
   */
  public Map<String, Zone> getZones() {
    return zones;
  }

  /**
   * @param zones
   *         Set the zones.
   */
  public Template setZones(Map<String, Zone> zones) {
    this.zones = zones;
    return this;
  }
}
