package com.recurrence.core;

import com.recurrence.contract.filters.PageOptions;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Monta a query string de uma requisição, ignorando valores nulos ou em branco.
 *
 * <pre>{@code
 * String path = QueryParams.create()
 *     .page(pageOptions)
 *     .add("status", "ACTIVE")
 *     .appendTo("/subscriptions");   // /subscriptions?offset=0&limit=20&status=ACTIVE
 * }</pre>
 */
public final class QueryParams {

    private final List<String> params = new ArrayList<>();

    private QueryParams() {}

    public static QueryParams create() {
        return new QueryParams();
    }

    public QueryParams add(String name, Object value) {
        if (value == null) {
            return this;
        }
        String rawValue = String.valueOf(value);
        if (rawValue.isBlank()) {
            return this;
        }
        params.add(encode(name) + "=" + encode(rawValue));
        return this;
    }

    /** Adiciona {@code offset} e {@code limit}, quando informados. */
    public QueryParams page(PageOptions pageOptions) {
        if (pageOptions == null) {
            return this;
        }
        return add("offset", pageOptions.getOffset())
                .add("limit", pageOptions.getLimit());
    }

    public String appendTo(String path) {
        return params.isEmpty() ? path : path + "?" + String.join("&", params);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
