package com.groupbyinc.api.tags;

import com.groupbyinc.api.AbstractBridge;
import com.groupbyinc.api.AbstractBridgeFactory;
import com.groupbyinc.api.Query;
import com.groupbyinc.api.model.ResultsBase;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public abstract class ResultsTagBase<R extends ResultsBase, B extends AbstractBridge<R>> extends TagSupport {
    private static final long serialVersionUID = 1L;

    private String var;
    protected String searchString;
    protected String sort;
    protected String refinements;
    protected String fields;
    protected String customUrlParams;
    protected String bridgeHost;
    protected int bridgePort;
    protected String area;
    protected String subCollection;
    protected String clientKey;

    protected AbstractBridgeFactory<R, B> factory;

    public ResultsTagBase(AbstractBridgeFactory<R, B> pFactory) {
        factory = pFactory;
    }

    protected R fireQuery() throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
        B bridge;
        if (StringUtils.isNotBlank(clientKey)) {
            bridge = factory.create(clientKey, bridgeHost, bridgePort);
        } else {
            bridge = factory.getBridge();
        }
        Query query = new Query().setSearchString(searchString).addRefinementsByString(refinements).setSort(sort)
                                 .addCustomUrlParamsByString(customUrlParams).addFieldsByString(fields)
                                 .setSubCollection(subCollection).setArea(area);
        return bridge.search(query);
    }

    @Override
    public int doStartTag() throws JspException {
        if (StringUtils.isBlank(var)) {
            throw new JspException("var must be specified.");
        }

        try {
            pageContext.setAttribute(var, fireQuery(), PageContext.REQUEST_SCOPE);
        } catch (Exception e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String pVar) {
        var = pVar;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String pQuery) {
        searchString = pQuery;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String pSort) {
        sort = pSort;
    }

    public String getRefinements() {
        return refinements;
    }

    public void setRefinements(String pFilter) {
        refinements = pFilter;
    }

    public String getCustomUrlParams() {
        return customUrlParams;
    }

    public void setCustomUrlParams(String pOther) {
        customUrlParams = pOther;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String pFields) {
        fields = pFields;
    }

    public String getBridgeHost() {
        return bridgeHost;
    }

    public void setBridgeHost(String pBridgeHost) {
        bridgeHost = pBridgeHost;
    }

    public int getBridgePort() {
        return bridgePort;
    }

    public void setBridgePort(int pBridgePort) {
        bridgePort = pBridgePort;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String pClientKey) {
        clientKey = pClientKey;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String pSubArea) {
        area = pSubArea;
    }

    public String getSubCollection() {
        return subCollection;
    }

    public void setSubCollection(String pSubCollection) {
        subCollection = pSubCollection;
    }

    @Override
    public void release() {
        super.release();
        var = null;
        customUrlParams = null;
        sort = null;
        searchString = null;
        fields = null;
        bridgeHost = null;
        clientKey = null;
        area = null;
        subCollection = null;
    }

}
