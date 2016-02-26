package com.groupbyinc.api.model;

import com.groupbyinc.api.model.zone.BannerZone;
import com.groupbyinc.api.model.zone.ContentZone;
import com.groupbyinc.api.model.zone.RecordZone;
import com.groupbyinc.api.model.zone.RichContentZone;
import com.groupbyinc.common.jackson.annotation.JsonProperty;
import com.groupbyinc.common.jackson.annotation.JsonSubTypes;
import com.groupbyinc.common.jackson.annotation.JsonTypeId;
import com.groupbyinc.common.jackson.annotation.JsonTypeInfo;

/**
 * @internal
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ContentZone.class, name = "Content"), @JsonSubTypes.Type(value = RecordZone.class, name = "Record"), @JsonSubTypes.Type(value = BannerZone.class, name = "Banner"),
    @JsonSubTypes.Type(value = RichContentZone.class, name = "Rich_Content")})
public abstract class Zone<Z extends Zone<Z>> {

  public enum Type {
    Content,
    Record,
    Banner,
    Rich_Content
  }

  @JsonProperty("_id") private String id;
  private String name;

  /**
   * <code>
   * Default constructor
   * </code>
   * @internal
   */
  public Zone() {
    // default constructor
  }

  /**
   * <code>
   * Zones support the following content types:
   *
   *     Content, Record, Banner, Rich_Content
   *
   * </code>
   *
   * @return The type of zone.
   */
  @JsonTypeId
  public abstract Type getType();

  /**
   * @return ID is a MD5 hash of the name.
   */
  public String getId() {
    return id;
  }

  /**
   * @param id Set the ID.
   * @return
   */
  @SuppressWarnings("unchecked")
  public Z setId(String id) {
    this.id = id;
    return (Z) this;
  }

  /**
   * @return The name of the zone.
   */
  public String getName() {
    return name;
  }

  /**
   * @param name Set the name
   * @return
   */
  @SuppressWarnings("unchecked")
  public Z setName(String name) {
    this.name = name;
    return (Z) this;
  }
}
