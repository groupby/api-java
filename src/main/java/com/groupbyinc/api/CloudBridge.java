package com.groupbyinc.api;

/**
 * @author Ben Teichman
 */
public class CloudBridge extends AbstractBridge {
    private static final String DOT = ".";
    private static final String CLOUD_HOST = "groupbycloud.com";
    private static final int CLOUD_PORT = 443;
    private static final String CLOUD_PATH = "/api/v1";
    private static final String URL_SUFFIX = DOT + CLOUD_HOST + COLON + CLOUD_PORT + CLOUD_PATH;

    /**
     * <code>
     * Constructor to create a bridge object that connects to the search api.
     * JSON Reference:
     * The key as found in your key management page in the command center
     * {"clientKey": "--clientKey--"}
     * </code>
     *
     * @param clientKey
     *         The key as found in your key management page in the command
     *         center.
     * @param customerId
     *         The name of your subdomain.  For example, https://--customerId--.groupbycloud.com
     */
    public CloudBridge(String clientKey, String customerId) {
        super(clientKey, HTTPS + customerId + URL_SUFFIX);
    }
}
