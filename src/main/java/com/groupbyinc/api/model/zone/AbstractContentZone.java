package com.groupbyinc.api.model.zone;

import com.groupbyinc.api.model.Zone;
import com.groupbyinc.common.jackson.annotation.JsonIgnore;

/**
 * @internal
 * @author will
 */
public abstract class AbstractContentZone<T extends AbstractContentZone<T>> extends Zone<T> {

  @JsonIgnore private String content;

  /**
   * @return If this zone is not a Record zone this will represent the value
   * set by the merchandiser.
   */
  protected String getContent() {
    return content;
  }

  /**
   * @param content Set the content
   * @return
   */
  @SuppressWarnings("unchecked")
  protected T setContent(String content) {
    this.content = content;
    return (T) this;
  }
}
