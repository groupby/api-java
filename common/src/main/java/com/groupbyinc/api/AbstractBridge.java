package com.groupbyinc.api;

import com.groupbyinc.api.model.AbstractRecord;
import com.groupbyinc.api.model.AbstractResults;
import com.groupbyinc.api.model.RefinementsResult;
import com.groupbyinc.api.request.AbstractRequest;
import com.groupbyinc.common.apache.commons.io.Charsets;
import com.groupbyinc.common.apache.commons.io.IOUtils;
import com.groupbyinc.common.apache.commons.lang3.StringUtils;
import com.groupbyinc.common.apache.http.HttpResponse;
import com.groupbyinc.common.apache.http.client.config.RequestConfig;
import com.groupbyinc.common.apache.http.client.methods.HttpPost;
import com.groupbyinc.common.apache.http.entity.StringEntity;
import com.groupbyinc.common.apache.http.impl.client.CloseableHttpClient;
import com.groupbyinc.common.apache.http.impl.client.HttpClientBuilder;
import com.groupbyinc.common.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

/**
 * <code>
 * The Bridge is the class responsible for marshalling a query to and from the search service.
 * Because the bridge holds a connection pool that is expensive to create, it is highly recommended
 * that the bridge is held in the application memory scope and reused where appropriate.
 * <b>Do not create a new bridge object for each request as you will incur overhead that will
 * bring down your UI servers when under heavy load!</b>
 * </code>
 *
 * @author Will Warren
 */
public abstract class AbstractBridge<RQ extends AbstractRequest<RQ>, //
        Q extends AbstractQuery<RQ, Q>,//
        D extends AbstractRecord<D>, //
        R extends AbstractResults<D, R>> {

    private static final Logger LOG = Logger.getLogger(AbstractBridge.class.getName());

    public static final String CLUSTER = "/cluster";
    protected static final String COLON = ":";
    protected static final String HTTP = "http://";
    protected static final String HTTPS = "https://";
    private static final String SEARCH = "/search";
    private static final String REFINEMENTS = "/refinements";
    private static final String REFINEMENT_SEARCH = "/refinement";
    private static final String BODY = "\nbody:\n";
    private static final String EXCEPTION_FROM_BRIDGE = "Exception from bridge: ";
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .setSocketTimeout(30000)
            .build();
    private final String bridgeUrl;
    private final String bridgeRefinementsUrl;
    private final String bridgeRefinementSearchUrl;
    private final String bridgeClusterUrl;
    protected String clientKey;
    private CloseableHttpClient httpClient;
    private long retryTimeout = 80;

    /**
     * <code>
     * Constructor to create a bridge object that connects to the search api.
     * JSON Reference:
     * The key as found in your key management page in the command center
     * {"clientKey": "<client key>"}
     * </code>
     *
     * @param clientKey
     *         The key as found in your key management page in the command
     *         center.
     * @param baseUrl
     *         The base url the bridge is serving on.
     */
    public AbstractBridge(String clientKey, String baseUrl) {
        this(clientKey, baseUrl, true);
    }

    /**
     * <code>
     * Constructor to create a bridge object that connects to the search api.
     * JSON Reference:
     * The key as found in your key management page in the command center
     * {"clientKey": "<client key>"}
     * </code>
     *
     * @param clientKey
     *         The key as found in your key management page in the command
     *         center.
     * @param baseUrl
     *         The base url the bridge is serving on.
     * @param compressResponse
     *         true to compress the response content, false to send uncompressed response.
     */
    public AbstractBridge(String clientKey, String baseUrl, boolean compressResponse) {
        try {
            new URI(baseUrl);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid url: " + baseUrl);
        }

        this.clientKey = clientKey;
        createClient(compressResponse);

        bridgeUrl = baseUrl + SEARCH;
        bridgeRefinementsUrl = bridgeUrl + REFINEMENTS;
        bridgeRefinementSearchUrl = baseUrl + REFINEMENT_SEARCH;
        bridgeClusterUrl = baseUrl + CLUSTER;
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
    public String getBridgeRefinementsUrl() {
        return bridgeRefinementsUrl;
    }

    /**
     * @return
     *
     * @internal
     */
    public String getClusterBridgeUrl() {
        return bridgeClusterUrl;
    }

    private void createClient(boolean compressResponse) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);

        HttpClientBuilder b = HttpClientBuilder.create();
        if (!compressResponse) {
            b.disableContentCompression();
        }
        httpClient = b.setConnectionManager(cm).setDefaultRequestConfig(REQUEST_CONFIG).build();
    }

    protected abstract R map(InputStream data, boolean returnBinary);

    protected abstract RefinementsResult mapRefinements(InputStream data, boolean returnBinary);

    /**
     * <code>
     * Connects to the search service, parses the response into a model
     * </code>
     *
     * @param query
     *         A query representing the search.
     *
     * @return Results object from the search service
     *
     * @throws IOException
     */
    public R search(Q query) throws IOException {
        InputStream data = fireRequest(getBridgeUrl(), query.getBridgeJson(clientKey), query.isReturnBinary());
        return map(data, query.isReturnBinary());
    }

    protected RefinementsResult refinements(Q query, String navigationName) throws IOException {
        InputStream data = fireRequest(
                getBridgeRefinementsUrl(), query.getBridgeRefinementsJson(clientKey, navigationName),
                query.isReturnBinary());
        return mapRefinements(data, query.isReturnBinary());
    }

    protected InputStream fireRequest(String url, String body, boolean returnBinary) throws IOException {
        HttpResponse response = postToBridge(url, body);
        InputStream data = response.getEntity().getContent();
        if (response.getStatusLine().getStatusCode() != 200) {
            String status = response.getStatusLine().toString();
            byte[] bytes = IOUtils.toByteArray(data);
            IOUtils.closeQuietly(data);
            handleErrorStatus(status, bytes, returnBinary);
        }
        return data;
    }

    void handleErrorStatus(String status, byte[] bytes, boolean returnBinary) throws IOException {
        StringBuilder msg = new StringBuilder();
        try {
            String errors = map(new ByteArrayInputStream(bytes), returnBinary).getErrors();
            if (StringUtils.isNotBlank(errors)) {
                msg.append(", ").append(errors);
            }
        } catch (Exception e) {
            LOG.warning("unable to parse error from response.");
        } finally {
            if (StringUtils.isBlank(msg)) {
                msg.append(BODY).append(StringUtils.toString(bytes, Charsets.UTF_8.name()));
            }
        }
        throw new IOException(EXCEPTION_FROM_BRIDGE + status + msg.toString());
    }

    private HttpResponse postToBridge(String url, String bridgeJson) throws IOException {
        StringEntity entity = new StringEntity(bridgeJson, Charset.forName("UTF-8"));
        entity.setContentType("application/json");

        HttpResponse response = null;
        boolean successful = false;
        int tries = 0;
        SocketException lastError = null;
        while (!successful && tries < 3) {
            try {
                HttpPost httpPost = new HttpPost(url + "?retry=" + tries);
                httpPost.setEntity(entity);
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
            }
        }
        if (tries < 3) {
            return response;
        }
        throw new IOException("Tried to connect three times to: " + url, lastError);
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
     * @param retryTimeout
     */
    public void setRetryTimeout(long retryTimeout) {
        this.retryTimeout = retryTimeout;
    }

}
