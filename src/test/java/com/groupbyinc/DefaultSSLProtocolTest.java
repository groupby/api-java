package com.groupbyinc;

import org.junit.Assert;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DefaultSSLProtocolTest {

    @Test
    public void testTLSVersionSupported() throws NoSuchAlgorithmException {
        Assert.assertTrue(Arrays.stream(SSLContext.getDefault().createSSLEngine().getEnabledProtocols())
                .filter(tls -> tls.equals("TLSv1.2") || tls.equals("TLSv1.3"))
                .findFirst().isPresent());
    }
}
