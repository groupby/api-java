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
 * - `type`: the content type
 *     - `Content`: a string of text.
 *     - `Rich_Content`: a string of HTML text.
 *     - `Records`: A list of zero or more records.
 *     - `Banner`: The URL to a banner image, as defined in the banner management section.
 * records matching a search.
 * </code>
 * @internal
 * @author will
 */
public class BannerZone extends AbstractContentZone<BannerZone> {

  /**
   * <code>
   * Default constructor
   * </code>
   */
  public BannerZone() {
    // default constructor
  }

  @Override
  public Type getType() {
    return Type.Banner;
  }

  @JsonProperty
  public String getBannerUrl() {
    return getContent();
  }

  public BannerZone setBannerUrl(String bannerUrl) {
    return setContent(bannerUrl);
  }
}
