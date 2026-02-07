package org.example.pagbank.request.payload;

public class Amount {

    private final int value;
    private final String currency;

    public Amount(int value) {
        this.value = value;
        this.currency = "BRL";
    }

    public int getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }
}
