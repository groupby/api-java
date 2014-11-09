package com.groupbyinc.api;

import com.groupbyinc.api.model.Results;

public class BridgeFactory extends AbstractBridgeFactory<Results, Bridge> {
    @Override
    public Bridge create(String pClientKey, String pBridgeHost, int pBridgePort) {
        return new Bridge(pClientKey, pBridgeHost, pBridgePort);
    }
}
