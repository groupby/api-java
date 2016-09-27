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
 * - `type`: `Content`: a string of text.
 *
 * </code>
 *
 * @author will
 */
public class ContentZone extends AbstractContentZone<ContentZone> {

  @Override
  public Type getType() {
    return Type.Content;
  }

  @JsonProperty
  public String getContent() {
    return super.getContent();
  }

  public ContentZone setContent(String content) {
    return super.setContent(content);
  }
}
