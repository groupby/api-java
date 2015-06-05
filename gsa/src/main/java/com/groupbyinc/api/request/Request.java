package com.groupbyinc.api.request;

/**
 * Request object for the api to send Bridge requests
 *
 * @author lonell
 * @internal
 */
public class Request extends AbstractRequest<Request> {
    private String accurateCounts;
    private String partialFields;
    private String requiredFields;

    public String getAccurateCounts() {
        return accurateCounts;
    }

    @SuppressWarnings("unchecked")
    public Request setAccurateCounts(String accurateCounts) {
        this.accurateCounts = accurateCounts;
        return this;
    }

    public String getRequiredFields() {
        return requiredFields;
    }

    @SuppressWarnings("unchecked")
    public Request setRequiredFields(String requiredFields) {
        this.requiredFields = requiredFields;
        return this;
    }

    public String getPartialFields() {
        return partialFields;
    }

    @SuppressWarnings("unchecked")
    public Request setPartialFields(String partialFields) {
        this.partialFields = partialFields;
        return this;
    }
}
