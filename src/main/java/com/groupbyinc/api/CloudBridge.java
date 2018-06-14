package com.groupbyinc.api;

import com.groupbyinc.api.config.ConnectionConfiguration;
import com.groupbyinc.common.apache.http.Header;
import com.groupbyinc.common.apache.http.message.BasicHeader;

import java.util.Iterator;

public class CloudBridge extends AbstractBridge {

  private static final String DOT = ".";
  private static final String CLOUD_HOST = "groupbycloud.com";
  private static final int CLOUD_PORT = 443;
  private static final String CLOUD_PATH = "/api/v1";
  private static final String URL_SUFFIX = DOT + CLOUD_HOST + COLON + CLOUD_PORT + CLOUD_PATH;

  /**
   * <code>
   * Constructor to create a bridge object that connects to the Search API.
   *
   * JSON Reference:
   * The key as found in your Key Management page in the command center
   *
   *     {"clientKey": "--clientKey--"}
   *
   * </code>
   *
   * @param clientKey
   *         The key as found in your Key Management page in the Command
   *         Center.
   * @param customerId
   *         The name of your subdomain.  For example, if your Command Center is at https://--customerId--.groupbycloud.com, this should be --customerId--
   */
  public CloudBridge(String clientKey, String customerId) {
    super(clientKey, HTTPS + customerId + URL_SUFFIX);
  }

  /**
   * <code>
   * Constructor to create a bridge object that connects to the search api.
   *
   * JSON Reference:
   * The key as found in your Key Management page in the command center
   *
   *     {"clientKey": "--clientKey--"}
   *
   * </code>
   *
   * @param clientKey
   *         The key as found in your Key Management page in the Command
   *         Center.
   * @param customerId
   *         The name of your subdomain.  The name of your subdomain.  For example, if your Command Center at is https://--customerId--.groupbycloud.com, this should be --customerId--
   * @param config
   *         Configuration for the underlying HttpClient instance.
   */
  public CloudBridge(String clientKey, String customerId, ConnectionConfiguration config) {
    super(clientKey, HTTPS + customerId + URL_SUFFIX, config);
  }

  /**
   * <code>
   * If using the semantic layer, this tells the semantic layer to not cache the response.
   * By default caching is on when using the semantic layer.
   *
   * Currently this is undocumented until the semantic layer is globally enabled.
   *
   * @param cachingEnabled
   *         Turn off caching by setting this to false.
   *
   * @internal
   */
  public synchronized void  setCachingEnabled(boolean cachingEnabled) {
    if (containsSkipCachingHeader()) {
      if (cachingEnabled) {
        removeSkipCachingHeader();
      }
    } else {
      if (!cachingEnabled) {
        addSkipCachingHeader();
      }
    }
  }

  private boolean containsSkipCachingHeader() {
    for (Header header : getHeaders()) {
      if (header.getName().equalsIgnoreCase("Skip-Caching")) {
        return true;
      }
    }
    return false;
  }

  private void removeSkipCachingHeader() {
    Iterator<Header> iterator = getHeaders().iterator();
    while (iterator.hasNext()) {
      Header header = iterator.next();
      if (header.getName().equalsIgnoreCase("Skip-Caching")) {
        iterator.remove();
      }
    }
  }

  private void addSkipCachingHeader() {
    getHeaders().add(new BasicHeader("Skip-Caching", "true"));
  }
}
