package com.groupbyinc.api.model.zone;

import com.groupbyinc.common.jackson.annotation.JsonProperty;

/**
 * <code>
 * Zone represents an area on the page that merchandisers have control over.  They can set the content
 * of a zone from the command center as part of a rule instantiation.  When a rule is triggered the zones are filled
 * in with the pertinent content.
 *
 * Zones contain the following properties:
 *
 * - `id`: an MD5 of the zone name
 * - `name`: the zone name
 * - `type`: `Rich_Content`: a string of HTML text.
 * </code>
 * @author will
 */
public class RichContentZone extends AbstractContentZone<RichContentZone> {

  /**
   * <code>
   * Default constructor
   * </code>
   */
  public RichContentZone() {
    // default constructor
  }

  @Override
  public Type getType() {
    return Type.Rich_Content;
  }

  @JsonProperty
  public String getRichContent() {
    return getContent();
  }

  public RichContentZone setRichContent(String content) {
    return setContent(content);
  }
}
