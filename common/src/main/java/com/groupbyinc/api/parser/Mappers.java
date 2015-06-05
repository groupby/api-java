package com.groupbyinc.api.parser;

import com.groupbyinc.common.jackson.annotation.JsonInclude.Include;
import com.groupbyinc.common.jackson.core.JsonFactory;
import com.groupbyinc.common.jackson.core.JsonParser;
import com.groupbyinc.common.jackson.core.JsonProcessingException;
import com.groupbyinc.common.jackson.core.type.TypeReference;
import com.groupbyinc.common.jackson.core.util.DefaultIndenter;
import com.groupbyinc.common.jackson.databind.DeserializationFeature;
import com.groupbyinc.common.jackson.databind.Module;
import com.groupbyinc.common.jackson.databind.ObjectMapper;
import com.groupbyinc.common.jackson.databind.ObjectReader;
import com.groupbyinc.common.jackson.databind.ObjectWriter;
import com.groupbyinc.common.jackson.dataformat.cbor.CBORFactory;
import com.groupbyinc.common.jackson.dataformat.cbor.CBORGenerator;
import com.groupbyinc.common.jackson.dataformat.smile.SmileFactory;
import com.groupbyinc.common.jackson.dataformat.smile.SmileGenerator;
import com.groupbyinc.common.jackson.dataformat.smile.SmileParser;
import com.groupbyinc.common.jackson.module.afterburner.AfterburnerModule;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class Mappers {
    public static final Charset BINARY = Charset.forName("ISO-8859-1");
    public static final Charset UTF8 = Charset.forName("UTF8");

    public static final ObjectMapper CBOR = createMapper(Type.CBOR);
    public static final ObjectMapper JSON = createMapper(Type.JSON);
    public static final ObjectMapper SMILE = createMapper(Type.SMILE);

    private enum Type {
        JSON,
        CBOR,
        SMILE
    }

    private static ObjectMapper createMapper(Type type) {
        JsonFactory factory;
        switch (type) {
        case CBOR:
            factory = new CBORFactory().enable(CBORGenerator.Feature.WRITE_MINIMAL_INTS).enable(
                    JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES).enable(
                    JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            break;
        case SMILE:
            factory = new SmileFactory().enable(SmileGenerator.Feature.CHECK_SHARED_NAMES).disable(
                    SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES).disable(
                    SmileGenerator.Feature.ENCODE_BINARY_AS_7BIT).disable(SmileGenerator.Feature.WRITE_END_MARKER)
                                        .enable(
                                                SmileGenerator.Feature.WRITE_HEADER).enable(
                            SmileParser.Feature.REQUIRE_HEADER);
            break;
        case JSON:
        default:
            factory = new JsonFactory().enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES).enable(
                    JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            break;
        }
        return new ObjectMapper(factory).registerModule(new AfterburnerModule()).setSerializationInclusion(
                Include.NON_NULL).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private Mappers() {
        // hide utility
    }

    private static ObjectMapper getMapper(boolean binary) {
        return binary ? CBOR : JSON;
    }

    private static ObjectWriter getWriter(boolean binary, boolean pretty) {
        return binary ? CBOR.writer() : getPrettyWriter(pretty);
    }

    private static ObjectWriter getPrettyWriter(boolean pretty) {
        return pretty ? JSON.writerWithDefaultPrettyPrinter() : JSON.writer();
    }

    public static ObjectReader getStrictReader(boolean binary) {
        return getMapper(binary).reader().with(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).with(
                DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    public static Charset getEncoding(boolean binary) {
        return binary ? BINARY : UTF8;
    }

    public static String getMimeType(boolean binary) {
        return binary ? "application/cbor" : "application/json";
    }

    public static byte[] writeValueAsBytes(Object object, boolean binary) {
        return writeValueAsBytes(object, binary, false);
    }

    public static byte[] writeValueAsBytes(Object object, boolean binary, boolean pretty) {
        try {
            ObjectWriter w = getWriter(binary, pretty);
            if (pretty && !binary) {
                return (w.writeValueAsString(object) + DefaultIndenter.SYS_LF).getBytes();
            } else {
                return getWriter(binary, pretty).writeValueAsBytes(object);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String writeValueAsString(Object object) {
        return writeValueAsString(object, false);
    }

    public static String writeValueAsString(Object object, boolean pretty) {
        try {
            String s = getWriter(false, pretty).writeValueAsString(object);
            if (pretty) {
                s = s + DefaultIndenter.SYS_LF;
            }
            return s;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(byte[] data, Class<T> clazz, boolean binary) {
        try {
            return getMapper(binary).readValue(data, clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(byte[] data, Class<T> clazz, Class<?> mixIn, boolean binary) {
        try {
            return getMapper(binary).copy().addMixIn(clazz, mixIn).reader(clazz).readValue(data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(byte[] data, Class<T> clazz, boolean binary, Module... modules) {
        try {
            ObjectMapper mapper = getMapper(binary).copy();
            for (Module m : modules) {
                mapper.registerModule(m);
            }
            return mapper.reader(clazz).readValue(data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(byte[] data, TypeReference<T> reference, boolean binary) {
        try {
            return getMapper(binary).readValue(data, reference);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(InputStream data, Class<T> clazz, boolean binary) {
        try {
            return getMapper(binary).readValue(data, clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(InputStream data, TypeReference<T> reference, boolean binary) {
        try {
            return getMapper(binary).readValue(data, reference);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T readValue(InputStream data, Class<T> clazz, boolean binary, Module... modules) {
        try {
            ObjectMapper mapper = getMapper(binary).copy();
            for (Module m : modules) {
                mapper.registerModule(m);
            }
            return mapper.reader(clazz).readValue(data);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String humanReadable(InputStream data, boolean binary) {
        try {
            return JSON.writeValueAsString(
                    getMapper(binary).readValue(
                            data, new TypeReference<Map<String, Object>>() {
                            }));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String humanReadable(byte[] data, boolean binary) {
        return humanReadable(data, binary, false);
    }

    public static String humanReadable(byte[] data, boolean binary, boolean pretty) {
        try {
            return getWriter(false, pretty).writeValueAsString(
                    getMapper(binary).readValue(
                            data, new TypeReference<Map<String, Object>>() {
                            }));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String humanReadableSmile(byte[] data) {
        try {
            return JSON.writeValueAsString(
                    SMILE.readValue(
                            data, new TypeReference<Map<String, Object>>() {
                            }));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
