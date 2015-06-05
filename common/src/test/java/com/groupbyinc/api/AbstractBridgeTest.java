package com.groupbyinc.api;

import com.groupbyinc.api.model.AbstractResults;
import com.groupbyinc.api.model.BaseResults;
import com.groupbyinc.api.model.RefinementsResult;
import com.groupbyinc.api.parser.Mappers;
import com.groupbyinc.common.http.HttpRequestInterceptor;
import com.groupbyinc.common.http.HttpResponseInterceptor;
import com.groupbyinc.common.http.client.protocol.RequestAcceptEncoding;
import com.groupbyinc.common.http.client.protocol.ResponseContentEncoding;
import com.groupbyinc.common.http.impl.client.CloseableHttpClient;
import com.groupbyinc.common.http.impl.execchain.ClientExecChain;
import com.groupbyinc.common.http.impl.execchain.RetryExec;
import com.groupbyinc.common.http.protocol.ImmutableHttpProcessor;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractBridgeTest {
    protected String clientKey = "clientKey";

    @Test
    public void testAcceptCompressedResponseByDefault() throws Exception {
        String bridgeUrl = "bridgeUrl";
        AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl) {
            @Override
            protected AbstractResults map(InputStream data, boolean returnBinary) {
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

    @Test
    public void testTurnOffCompressedResponse() throws Exception {
        String bridgeUrl = "bridgeUrl";
        AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, false) {
            @Override
            protected AbstractResults map(InputStream data, boolean returnBinary) {
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
        assertFalse("The bridge should not accept gzip response.", foundRequestAcceptEncoding);

        HttpResponseInterceptor[] responseInterceptors = getHttpResponseInterceptors(httpProcessor);
        boolean foundResponseContentEncoding = findResponseContentEncoding(responseInterceptors);
        assertFalse("The bridge should not accept gzip response.", foundResponseContentEncoding);
    }

    @Test
    public void testTurnOnCompressedResponse() throws Exception {
        String bridgeUrl = "bridgeUrl";
        AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, true) {
            @Override
            protected AbstractResults map(InputStream data, boolean returnBinary) {
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

    @Test
    public void testHandleErrorStatus() {
        String bridgeUrl = "bridgeUrl";
        AbstractBridge bridge = new AbstractBridge(clientKey, bridgeUrl, true) {
            @Override
            protected AbstractResults map(InputStream data, boolean returnBinary) {
                return Mappers.readValue(data, BaseResults.class, returnBinary);
            }

            @Override
            protected RefinementsResult mapRefinements(InputStream data, boolean returnBinary) {
                return null;
            }
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
            assertEquals("Exception from bridge: status\n" +
                    "body:\n" +
                    "non-json error message", e.getMessage());
        }

        byte[] bytes = new byte[]{0x38, 0x29, 0x49};
        try {
            bridge.handleErrorStatus(status, bytes, false);
        } catch (IOException e) {
            assertEquals("Exception from bridge: status\n" +
                    "body:\n" +
                    "8)I", e.getMessage());
        }
    }

    private ImmutableHttpProcessor getImmutableHttpProcessor(AbstractBridge bridge)
            throws NoSuchFieldException, IllegalAccessException {
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

    private HttpRequestInterceptor[] getHttpRequestInterceptors(ImmutableHttpProcessor httpProcessor)
            throws NoSuchFieldException, IllegalAccessException {
        Field requestInterceptorsField = httpProcessor.getClass().getDeclaredField("requestInterceptors");
        requestInterceptorsField.setAccessible(true);
        return (HttpRequestInterceptor[]) requestInterceptorsField.get(httpProcessor);
    }

    private HttpResponseInterceptor[] getHttpResponseInterceptors(ImmutableHttpProcessor httpProcessor)
            throws NoSuchFieldException, IllegalAccessException {
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

    private boolean findRequestAcceptEncoding(HttpRequestInterceptor[] requestInterceptors) {
        boolean foundRequestAcceptEncoding = false;
        for (HttpRequestInterceptor requestInterceptor : requestInterceptors) {
            if (RequestAcceptEncoding.class.equals(requestInterceptor.getClass())) {
                foundRequestAcceptEncoding = true;
            }
        }
        return foundRequestAcceptEncoding;
    }
}