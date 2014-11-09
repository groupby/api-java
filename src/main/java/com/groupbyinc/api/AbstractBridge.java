package com.groupbyinc.api;

import com.groupbyinc.api.model.Navigation;
import com.groupbyinc.api.model.ResultsBase;
import com.groupbyinc.api.parser.Mappers;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Logger;

/**
 * <code>
 * Parent class for the Bridge.  Please use the Bridge to instantiate your bridge objects.
 * </code>
 *
 * @author will
 * @internal
 */
public abstract class AbstractBridge<R extends ResultsBase> {
    private static final Logger LOG = Logger.getLogger(AbstractBridge.class.getName());
    private static final String COLON = ":";
    private static final String SEARCH = "/search";
    private static final String HTTP = "http://";
    private static final String BODY = "\nbody:\n";
    private static final String EXCEPTION_FROM_BRIDGE = "Exception from bridge: ";
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setConnectTimeout(15000)
                                                                     .setConnectionRequestTimeout(15000)
                                                                     .setSocketTimeout(30000).build();

    private CloseableHttpClient httpClient;
    protected String clientKey;
    private long retryTimeout = 80;

    private final String bridgeUrl;
    private final String bridgeClusterUrl;

    /**
     * @param pClientKey
     * @param pBridgeHost
     * @param pBridgePort
     *
     * @return
     *
     * @internal
     */
    protected AbstractBridge(String pClientKey, String pBridgeHost, int pBridgePort) {
        clientKey = pClientKey;
        createClient();
        if (pBridgeHost == null) {
            throw new IllegalStateException("Bridge host must be supplied");
        }

        String baseUrl = HTTP + pBridgeHost + COLON + pBridgePort;
        bridgeUrl = baseUrl + SEARCH;
        bridgeClusterUrl = baseUrl + "/cluster";
    }

    /**
     * @return
     *
     * @internal
     */
    public String getBridgeUrl() {
        return bridgeUrl;
    }

    /**
     * @return
     *
     * @internal
     */
    public String getClusterBridgeUrl() {
        return bridgeClusterUrl;
    }

    private void createClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);

        HttpClientBuilder b = HttpClientBuilder.create();
        httpClient = b.setConnectionManager(cm).setDefaultRequestConfig(REQUEST_CONFIG).build();
    }

    protected abstract R map(InputStream pData, boolean pReturnBinary);

    /**
     * Connects to the bridge, parses the response into a model and returns on
     * the refinement matches
     *
     * @param pQuery
     *
     * @return
     *
     * @throws ClientProtocolException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @deprecated
     */
    @Deprecated
    public List<Navigation> searchRefinements(Query pQuery) // NOSONAR
            throws IOException {
        InputStream response = fireRequest(
                getBridgeUrl(), pQuery.getBridgeJsonFilterSearch(clientKey), pQuery.isReturnBinary());
        return map(response, pQuery.isReturnBinary()).getAvailableNavigation();
    }

    /**
     * <code>
     * Connects to the bridge, parses the response into a model
     * </code>
     *
     * @param pQuery
     *         A query to send to the bridge.
     *
     * @return Results object from the bridge
     *
     * @throws java.io.IOException
     */
    public R search(Query pQuery) throws IOException {
        InputStream response = fireRequest(getBridgeUrl(), pQuery.getBridgeJson(clientKey), pQuery.isReturnBinary());
        return map(response, pQuery.isReturnBinary());
    }

    /**
     * <code>
     * Connects to the bridge and performs a cluster search, parses the response
     * into a model
     * </code>
     *
     * @param pQuery
     *         A query to send to the bridge
     *
     * @return Results object from the bridge
     *
     * @throws java.io.IOException
     */
    public R searchCluster(Query pQuery) throws IOException {
        InputStream response = fireRequest(
                getClusterBridgeUrl(), pQuery.getBridgeJson(clientKey), pQuery.isReturnBinary());
        return map(response, pQuery.isReturnBinary());
    }

    private InputStream fireRequest(String pUrl, String pBody, boolean pReturnBinary) throws IOException {
        HttpPost httpPost = new HttpPost(pUrl);
        HttpResponse response = postToBridge(httpPost, pBody);

        InputStream data = response.getEntity().getContent();
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException(
                    EXCEPTION_FROM_BRIDGE + response.getStatusLine().toString() + BODY + Mappers.humanReadable(
                            data, pReturnBinary));
        }
        return data;
    }

    private HttpResponse postToBridge(HttpPost httpPost, String bridgeJson)
            throws ClientProtocolException, IOException {
        StringEntity entity = new StringEntity(bridgeJson, Charset.forName("UTF-8"));
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        HttpResponse response = null;
        boolean successful = false;
        int tries = 0;
        SocketException lastError = null;
        while (!successful && tries < 3) {
            try {
                response = httpClient.execute(httpPost);
                successful = true;
            } catch (SocketException e) {
                try {
                    Thread.sleep(retryTimeout);
                } catch (InterruptedException e1) {
                    // do nothing.
                }
                LOG.warning("Connection failed, retrying");
                lastError = e;
                tries++;
                httpPost.reset();

            }
        }
        if (tries < 3) {
            return response;
        }
        throw new IOException("Tried to connect three times to: " + httpPost.getURI(), lastError);
    }

    /**
     * <code>
     * Cleanup HTTP connection pool.
     * </code>
     */
    public void shutdown() {
        IOUtils.closeQuietly(httpClient);
    }

    /**
     * <code>
     * Sets the retry timeout for a failed request.
     * </code>
     *
     * @param pRetryTimeout
     */
    public void setRetryTimeout(long pRetryTimeout) {
        retryTimeout = pRetryTimeout;
    }

}
