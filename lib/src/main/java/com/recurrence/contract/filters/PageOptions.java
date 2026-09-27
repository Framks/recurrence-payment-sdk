package com.recurrence.contract.filters;

/**
 * Parâmetros de paginação para operações de listagem.
 *
 * <p>{@code offset} é baseado em zero e {@code limit} é a quantidade máxima de
 * itens da página. Ambos são opcionais: quando não informados, o adaptador
 * aplica o padrão do provedor.</p>
 *
 * <pre>{@code
 * PageOptions page = PageOptions.builder()
 *     .offset(0)
 *     .limit(20)
 *     .build();
 * }</pre>
 */
public final class PageOptions {

    private final Integer offset;
    private final Integer limit;

    private PageOptions(Builder builder) {
        this.offset = builder.offset;
        this.limit = builder.limit;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PageOptions of(Integer offset, Integer limit) {
        return builder().offset(offset).limit(limit).build();
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean hasOffset() {
        return offset != null;
    }

    public boolean hasLimit() {
        return limit != null;
    }

    public static final class Builder {
        private Integer offset;
        private Integer limit;

        private Builder() { }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public PageOptions build() {
            if (offset != null && offset < 0) {
                throw new IllegalArgumentException("offset must be >= 0");
            }
            if (limit != null && limit < 1) {
                throw new IllegalArgumentException("limit must be >= 1");
            }
            return new PageOptions(this);
        }
    }
}
