package com.recurrence.contract.filters;

import com.recurrence.contract.domain.commons.PaymentMethodType;
import java.time.OffsetDateTime;
import java.util.List;

// ToDo a pasta de infra não é o melhor canto de isso estar, deveria ter um pacote proprio para os filters?
public class SubscriptionFilter {
    private final Integer offset;
    private final Integer limit;
    private final List<String> status;
    private final List<PaymentMethodType> paymentMethods;
    private final OffsetDateTime createdAtStart;
    private final OffsetDateTime createdAtEnd;
    private final String query;
    private final String referenceId;

    private SubscriptionFilter(Builder builder) {
        this.offset = builder.offset;
        this.limit = builder.limit;
        this.status = builder.status;
        this.paymentMethods = builder.paymentMethods;
        this.createdAtStart = builder.createdAtStart;
        this.createdAtEnd = builder.createdAtEnd;
        this.query = builder.query;
        this.referenceId = builder.referenceId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public List<String> getStatus() {
        return status;
    }

    public List<PaymentMethodType> getPaymentMethods() {
        return paymentMethods;
    }

    public OffsetDateTime getCreatedAtStart() {
        return createdAtStart;
    }

    public OffsetDateTime getCreatedAtEnd() {
        return createdAtEnd;
    }

    public String getQuery() {
        return query;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public static final class Builder {
        private Integer offset;
        private Integer limit;
        private List<String> status;
        private List<PaymentMethodType> paymentMethods;
        private OffsetDateTime createdAtStart;
        private OffsetDateTime createdAtEnd;
        private String query;
        private String referenceId;

        private Builder() {}

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder status(List<String> status) {
            this.status = status;
            return this;
        }

        public Builder paymentMethods(List<PaymentMethodType> paymentMethods) {
            this.paymentMethods = paymentMethods;
            return this;
        }

        public Builder createdAtStart(OffsetDateTime createdAtStart) {
            this.createdAtStart = createdAtStart;
            return this;
        }

        public Builder createdAtEnd(OffsetDateTime createdAtEnd) {
            this.createdAtEnd = createdAtEnd;
            return this;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public SubscriptionFilter build() {
            return new SubscriptionFilter(this);
        }
    }
}
