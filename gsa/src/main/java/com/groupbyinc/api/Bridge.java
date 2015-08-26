package com.groupbyinc.api;

import com.groupbyinc.api.model.AbstractRecord;
import com.groupbyinc.api.model.Record;
import com.groupbyinc.api.model.RefinementsResult;
import com.groupbyinc.api.model.Results;
import com.groupbyinc.api.request.Request;
import com.groupbyinc.common.jackson.core.Version;
import com.groupbyinc.common.jackson.databind.Module;
import com.groupbyinc.common.jackson.databind.module.SimpleModule;
import com.groupbyinc.utils.Mappers;

import java.io.IOException;
import java.io.InputStream;

public class Bridge extends AbstractBridge<Request, Query, Record, Results> {
    public static Module RECORD_MAPPER = new SimpleModule("RecordModule", new Version(1, 0, 0, null, null, null))
            .addAbstractTypeMapping(AbstractRecord.class, Record.class);

    public Bridge(String clientKey, String bridgeHost, int bridgePort) {
        this(clientKey, bridgeHost, bridgePort, false);
    }

    public Bridge(String clientKey, String bridgeHost, int bridgePort, boolean secure) {
        this(clientKey, (secure ? HTTPS : HTTP) + bridgeHost + COLON + bridgePort);
    }

    protected Bridge(String clientKey, String baseUrl) {
        super(clientKey, baseUrl);
    }

    @Override
    protected Results map(InputStream data, boolean returnBinary) {
        return Mappers.readValue(data, Results.class, returnBinary, RECORD_MAPPER);
    }

    @Override
    protected RefinementsResult mapRefinements(InputStream data, boolean returnBinary) {
        return Mappers.readValue(data, RefinementsResult.class, returnBinary, RECORD_MAPPER);
    }

    /**
     * <code>
     * Connects to the search service and performs a cluster search, parses the response
     * into a model
     * </code>
     *
     * @param query A query to send to the search service
     * @return Results object from the bridge
     * @throws IOException
     */
    public Results searchCluster(Query query) throws IOException {
        InputStream response = fireRequest(
                getClusterBridgeUrl(), query.getBridgeJson(clientKey), query.isReturnBinary());
        return map(response, query.isReturnBinary());
    }
}
