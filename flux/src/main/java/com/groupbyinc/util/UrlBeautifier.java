package com.groupbyinc.util;

import com.groupbyinc.api.Query;
import com.groupbyinc.api.request.Request;
import com.groupbyinc.injector.StaticInjector;
import com.groupbyinc.injector.StaticInjectorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author will
 */
public class UrlBeautifier extends AbstractUrlBeautifier<Request, Query> {
    static final StaticInjector<Map<String, UrlBeautifier>> INJECTOR =
            new StaticInjectorFactory<Map<String, UrlBeautifier>>().create();

    static {
        INJECTOR.set(new HashMap<String, UrlBeautifier>());
    }

    private UrlBeautifier() {
    }

    /**
     * <code>
     * Create a UrlBeautifier and store it for the lifetime of this JVM under the name specified.
     * </code>
     *
     * @param name The handle back to this UrlBeautifier
     */
    public static void createUrlBeautifier(String name) {
        getUrlBeautifiers().put(name, new UrlBeautifier());
    }

    /**
     * <code>
     * Get a map of UrlBeautifiers keyed by name.
     * </code>
     *
     * @return
     */
    public static Map<String, UrlBeautifier> getUrlBeautifiers() {
        return INJECTOR.get();
    }

    @Override
    protected Query createQuery() {
        return new Query();
    }
}
