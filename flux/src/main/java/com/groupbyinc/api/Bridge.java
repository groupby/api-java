package com.groupbyinc.api;

import com.groupbyinc.api.model.AbstractRecord;
import com.groupbyinc.api.model.Record;
import com.groupbyinc.api.model.RefinementsResult;
import com.groupbyinc.api.model.Results;
import com.groupbyinc.api.request.Request;
import com.groupbyinc.common.jackson.core.Version;
import com.groupbyinc.common.jackson.databind.Module;
import com.groupbyinc.common.jackson.databind.module.SimpleModule;
import com.groupbyinc.common.jackson.util.Mappers;

import java.io.IOException;
import java.io.InputStream;

/**
 * ==============================================================================
 * All addition to this class should also be made to CloudBridge until such time
 * that the documentation server can handle more than two levels of inheritance
 * ==============================================================================
 *
 * @internal
 */
public class Bridge extends AbstractBridge<Request, Query, Record, Results> {

    public static Module RECORD_MAPPER = new SimpleModule("RecordModule", new Version(1, 0, 0, null, null, null)).addAbstractTypeMapping(AbstractRecord.class, Record.class);

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
     * Connects to the refinements service, parses the response into a model
     * Retrieves at most 10,000 refinements for the navigation specified.
     * </code>
     *
     * @param query
     *         A query representing the search.
     * @param navigationName
     *         The name of the navigation to get more refinements for.
     *
     * @return RefinementsResult object from the refinements service
     *
     * @throws IOException
     */
    @Override
    public RefinementsResult refinements(Query query, String navigationName) throws IOException {
        return super.refinements(query, navigationName);
    }

}
