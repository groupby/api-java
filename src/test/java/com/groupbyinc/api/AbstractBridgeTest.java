package com.groupbyinc.api;

import com.groupbyinc.api.model.RefinementsResult;
import com.groupbyinc.api.model.Results;
import com.groupbyinc.api.request.RefinementsRequest;
import com.groupbyinc.api.request.Request;
import com.groupbyinc.api.request.refinement.SelectedRefinementValue;
import com.groupbyinc.common.apache.http.HttpRequestInterceptor;
import com.groupbyinc.common.apache.http.HttpResponseInterceptor;
import com.groupbyinc.common.apache.http.client.protocol.RequestAcceptEncoding;
import com.groupbyinc.common.apache.http.client.protocol.ResponseContentEncoding;
import com.groupbyinc.common.apache.http.impl.client.CloseableHttpClient;
import com.groupbyinc.common.apache.http.impl.execchain.ClientExecChain;
import com.groupbyinc.common.apache.http.impl.execchain.RetryExec;
import com.groupbyinc.common.apache.http.protocol.ImmutableHttpProcessor;
import com.meterware.pseudoserver.PseudoServer;
import com.meterware.pseudoserver.PseudoServlet;
import com.meterware.pseudoserver.WebResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractBridgeTest {

  protected String clientKey = "clientKey";

  private PseudoServer server;

  @Before
  public void setUp() {
    server = new PseudoServer();
  }

  @After
  public void tearDown() {
    server.shutDown();
  }

  @Test
  public void testBridgeWithRequest() throws IOException {
    final List<String> postBodys = new ArrayList<String>();
    String bridgeUrl = "http://localhost:" + server.getConnectedPort();
    server.setResource("/search?pretty=true&retry=0", new PseudoServlet() {
      @Override
      public WebResource getResponse(String methodType) throws IOException {
        postBodys.add(new String(getBody()));
        return new WebResource("{}");
      }
    });
    AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, true) {
    };
    Query query = new Query();
    query.setReturnBinary(false);
    query.addValueRefinement("name", "value", true);
    query.getQueryUrlParams().put("pretty", "true");
    bridge.search(query);
    Request request = new Request();
    request.setReturnBinary(false);
    request.getRefinements().add(new SelectedRefinementValue().setNavigationName("name").setValue("value").setExclude(true));
    request.getQueryUrlParams().put("pretty", "true");
    bridge.search(request);
    assertEquals(2, postBodys.size());
    String queryPostBody = postBodys.get(0);
    String requestPostBody = postBodys.get(1);
    assertEquals(queryPostBody, requestPostBody);
  }

  @Test
  public void testBridgeWithRefinementsRequest() throws IOException {
    final List<String> postBodys = new ArrayList<String>();
    String bridgeUrl = "http://localhost:" + server.getConnectedPort();
    server.setResource("/search/refinements?pretty=true&retry=0", new PseudoServlet() {
      @Override
      public WebResource getResponse(String methodType) throws IOException {
        postBodys.add(new String(getBody()));
        return new WebResource("{}");
      }
    });
    AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, true) {
    };
    Query query = new Query();
    query.setReturnBinary(false);
    query.addValueRefinement("name", "value", true);
    query.getQueryUrlParams().put("pretty", "true");
    bridge.refinements(query, "brand");
    RefinementsRequest request = new RefinementsRequest();
    request.setOriginalQuery(new Request());
    request.setNavigationName("brand");
    request.getOriginalQuery().setReturnBinary(false);
    request.getOriginalQuery().getRefinements().add(new SelectedRefinementValue().setNavigationName("name").setValue("value").setExclude(true));
    request.getOriginalQuery().getQueryUrlParams().put("pretty", "true");
    bridge.refinements(request);
    assertEquals(2, postBodys.size());
    String queryPostBody = postBodys.get(0);
    String requestPostBody = postBodys.get(1);
    assertEquals(queryPostBody, requestPostBody);
  }

  @Test
  public void testAcceptCompressedResponseByDefault() throws Exception {
    String bridgeUrl = "bridgeUrl";
    AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl) {
      @Override
      protected Results map(InputStream data, boolean returnBinary) {
        return null;
      }

      @Override
      protected RefinementsResult mapRefinements(InputStream data, boolean returnBinary) {
        return null;
      }
    };

    ImmutableHttpProcessor httpProcessor = getImmutableHttpProcessor(bridge);

    HttpRequestInterceptor[] requestInterceptors = getHttpRequestInterceptors(httpProcessor);
    boolean foundRequestAcceptEncoding = findRequestAcceptEncoding(requestInterceptors);
    assertTrue("By default the bridge should accept gzip response.", foundRequestAcceptEncoding);

    HttpResponseInterceptor[] responseInterceptors = getHttpResponseInterceptors(httpProcessor);
    boolean foundResponseContentEncoding = findResponseContentEncoding(responseInterceptors);
    assertTrue("By default the bridge should accept gzip response.", foundResponseContentEncoding);
  }

  private ImmutableHttpProcessor getImmutableHttpProcessor(AbstractBridge bridge) throws NoSuchFieldException, IllegalAccessException {
    Field httpClientField = AbstractBridge.class.getDeclaredField("httpClient");
    httpClientField.setAccessible(true);

    CloseableHttpClient httpClient = (CloseableHttpClient) httpClientField.get(bridge);
    Field execChainField = httpClient.getClass().getDeclaredField("execChain");
    execChainField.setAccessible(true);

    ClientExecChain execChain = (ClientExecChain) execChainField.get(httpClient);
    Field requestExecutorField = execChain.getClass().getDeclaredField("requestExecutor");
    requestExecutorField.setAccessible(true);

    RetryExec executor = (RetryExec) requestExecutorField.get(execChain);
    requestExecutorField = executor.getClass().getDeclaredField("requestExecutor");
    requestExecutorField.setAccessible(true);

    ClientExecChain clientExecChain = (ClientExecChain) requestExecutorField.get(executor);
    Field httpProcessorField = clientExecChain.getClass().getDeclaredField("httpProcessor");
    httpProcessorField.setAccessible(true);

    return (ImmutableHttpProcessor) httpProcessorField.get(clientExecChain);
  }

  private HttpRequestInterceptor[] getHttpRequestInterceptors(ImmutableHttpProcessor httpProcessor) throws NoSuchFieldException, IllegalAccessException {
    Field requestInterceptorsField = httpProcessor.getClass().getDeclaredField("requestInterceptors");
    requestInterceptorsField.setAccessible(true);
    return (HttpRequestInterceptor[]) requestInterceptorsField.get(httpProcessor);
  }

  private boolean findRequestAcceptEncoding(HttpRequestInterceptor[] requestInterceptors) {
    boolean foundRequestAcceptEncoding = false;
    for (HttpRequestInterceptor requestInterceptor : requestInterceptors) {
      if (RequestAcceptEncoding.class.equals(requestInterceptor.getClass())) {
        foundRequestAcceptEncoding = true;
      }
    }
    return foundRequestAcceptEncoding;
  }

  private HttpResponseInterceptor[] getHttpResponseInterceptors(ImmutableHttpProcessor httpProcessor) throws NoSuchFieldException, IllegalAccessException {
    Field responseInterceptorsField = httpProcessor.getClass().getDeclaredField("responseInterceptors");
    responseInterceptorsField.setAccessible(true);
    return (HttpResponseInterceptor[]) responseInterceptorsField.get(httpProcessor);
  }

  private boolean findResponseContentEncoding(HttpResponseInterceptor[] responseInterceptors) {
    boolean foundResponseContentEncoding = false;
    for (HttpResponseInterceptor responseInterceptor : responseInterceptors) {
      if (ResponseContentEncoding.class.equals(responseInterceptor.getClass())) {
        foundResponseContentEncoding = true;
      }
    }
    return foundResponseContentEncoding;
  }

  @Test
  public void testTurnOffCompressedResponse() throws Exception {
    String bridgeUrl = "bridgeUrl";
    AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, false) {
    };

    ImmutableHttpProcessor httpProcessor = getImmutableHttpProcessor(bridge);

    HttpRequestInterceptor[] requestInterceptors = getHttpRequestInterceptors(httpProcessor);
    boolean foundRequestAcceptEncoding = findRequestAcceptEncoding(requestInterceptors);
    assertFalse("The bridge should not accept gzip response.", foundRequestAcceptEncoding);

    HttpResponseInterceptor[] responseInterceptors = getHttpResponseInterceptors(httpProcessor);
    boolean foundResponseContentEncoding = findResponseContentEncoding(responseInterceptors);
    assertFalse("The bridge should not accept gzip response.", foundResponseContentEncoding);
  }

  @Test
  public void testTurnOnCompressedResponse() throws Exception {
    String bridgeUrl = "bridgeUrl";
    AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, true) {
    };

    ImmutableHttpProcessor httpProcessor = getImmutableHttpProcessor(bridge);

    HttpRequestInterceptor[] requestInterceptors = getHttpRequestInterceptors(httpProcessor);
    boolean foundRequestAcceptEncoding = findRequestAcceptEncoding(requestInterceptors);
    assertTrue("By default the bridge should accept gzip response.", foundRequestAcceptEncoding);

    HttpResponseInterceptor[] responseInterceptors = getHttpResponseInterceptors(httpProcessor);
    boolean foundResponseContentEncoding = findResponseContentEncoding(responseInterceptors);
    assertTrue("By default the bridge should accept gzip response.", foundResponseContentEncoding);
  }

  @Test
  public void testGenerateURI() throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    assertURI("https://bridgeUrl:5000/search?retry=1", params, 1);

    params.put("novalue", null);
    assertURI("https://bridgeUrl:5000/search?novalue&retry=2", params, 2);

    params.put("key1", "value1");
    assertURI("https://bridgeUrl:5000/search?key1=value1&novalue&retry=3", params, 3);

    params.put("key2", "value2");
    assertURI("https://bridgeUrl:5000/search?key1=value1&key2=value2&novalue&retry=4", params, 4);
  }

  private void assertURI(String expectedUri, Map<String, String> params, int tries) throws Exception {
    String bridgeUrl = "https://bridgeUrl:5000";
    AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, true) {
    };
    assertEquals(expectedUri, bridge.generateURI(bridge.getBridgeUrl(), params, tries).toASCIIString());
  }

  @Test
  public void testHandleErrorStatus() {
    String bridgeUrl = "bridgeUrl";
    AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, true) {
    };
    String jsonContent = "{\"errors\":\"error message\"}";
    String status = "status";

    try {
      bridge.handleErrorStatus(status, jsonContent.getBytes(), false);
    } catch (IOException e) {
      assertEquals("Exception from bridge: status, error message", e.getMessage());
    }

    String content = "non-json error message";
    try {
      bridge.handleErrorStatus(status, content.getBytes(), false);
    } catch (IOException e) {
      assertEquals("Exception from bridge: status\n" + "body:\n" + "non-json error message", e.getMessage());
    }

    byte[] bytes = new byte[]{0x38, 0x29, 0x49};
    try {
      bridge.handleErrorStatus(status, bytes, false);
    } catch (IOException e) {
      assertEquals("Exception from bridge: status\n" + "body:\n" + "8)I", e.getMessage());
    }
  }
}
