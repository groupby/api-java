package com.groupbyinc.api.parser;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import com.fasterxml.jackson.dataformat.smile.SmileParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class Mappers {
    public static final Charset BINARY = Charset.forName("ISO-8859-1");
    public static final Charset UTF8 = Charset.forName("UTF8");

    public static final ObjectMapper SMILE = new ObjectMapper(
            new SmileFactory().enable(SmileGenerator.Feature.CHECK_SHARED_NAMES).disable(
                    SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES).disable(
                    SmileGenerator.Feature.ENCODE_BINARY_AS_7BIT).disable(SmileGenerator.Feature.WRITE_END_MARKER)
                              .enable(
                                      SmileGenerator.Feature.WRITE_HEADER).enable(SmileParser.Feature.REQUIRE_HEADER))
            .setSerializationInclusion(
                    Include.NON_NULL).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    public static final ObjectMapper JSON = new ObjectMapper(
            new JsonFactory().enable(
                    JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES).enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES))
            .setSerializationInclusion(Include.NON_NULL).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private Mappers() {
        // hide utility
    }

    private static ObjectMapper getMapper(boolean pBinary) {
        return pBinary ? SMILE : JSON;
    }

    public static Charset getEncoding(boolean pBinary) {
        return pBinary ? BINARY : UTF8;
    }

    public static byte[] writeValueAsBytes(Object pObject, boolean pBinary) {
        try {
            return getMapper(pBinary).writeValueAsBytes(pObject);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String writeValueAsString(Object pObject) {
        return humanReadable(writeValueAsBytes(pObject, true), true);
    }

    public static <T> T readValue(byte[] pData, Class<T> pClass, boolean pBinary) {
        try {
            return getMapper(pBinary).readValue(pData, pClass);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(byte[] pData, Class<T> pClass, Class<?> pMixIn, boolean pBinary) {
        try {
            ObjectMapper mapper = getMapper(pBinary).copy();
            mapper.addMixInAnnotations(pClass, pMixIn);
            return mapper.reader(pClass).readValue(pData);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(byte[] pData, TypeReference<T> pReference, boolean pBinary) {
        try {
            return getMapper(pBinary).readValue(pData, pReference);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(InputStream pData, Class<T> pClass, boolean pBinary) {
        try {
            return getMapper(pBinary).readValue(pData, pClass);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(InputStream pData, TypeReference<T> pReference, boolean pBinary) {
        try {
            return getMapper(pBinary).readValue(pData, pReference);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String humanReadable(InputStream pData, boolean pBinary) {
        try {
            return JSON.writeValueAsString(
                    getMapper(pBinary).readValue(
                            pData, new TypeReference<Map<String, Object>>() {
                            }));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String humanReadable(byte[] pData, boolean pBinary) {
        try {
            return JSON.writeValueAsString(
                    getMapper(pBinary).readValue(
                            pData, new TypeReference<Map<String, Object>>() {
                            }));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
