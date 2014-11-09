package com.groupbyinc.api;

import com.groupbyinc.api.model.Results;
import com.groupbyinc.api.parser.Mappers;

import java.io.InputStream;

/**
 * <code>
 * The Bridge is the class responsible for marshalling a query to and from the bridge service.
 * </code>
 *
 * @author will
 */
public class Bridge extends AbstractBridge<Results> {
    /**
     * <code>
     * JSON Reference:
     * <p/>
     * The key as found in your key management page in the command center
     * <p/>
     * {c: '<client key>'}
     * </code>
     *
     * @param pClientKey
     *         The key as found in your key management page in the command
     *         center.
     * @param pBridgeHost
     *         The host of the bridge you wish to connect to.
     * @param pBridgePort
     *         The port the bridge is serving on.
     */
    public Bridge(String pClientKey, String pBridgeHost, int pBridgePort) {
        super(pClientKey, pBridgeHost, pBridgePort);
    }

    @Override
    protected Results map(InputStream pData, boolean pReturnBinary) {
        return Mappers.readValue(pData, Results.class, pReturnBinary);
    }
}
