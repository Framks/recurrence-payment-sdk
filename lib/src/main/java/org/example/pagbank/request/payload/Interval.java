package org.example.pagbank.request.payload;

public class Interval {

    private int length;
    private String unit;

    public Interval(int length) {
        this.length = length;
        this.unit = "MONTH";
    }

    public int getLength() {
        return length;
    }

    public String getUnit() {
        return unit;
    }
}
