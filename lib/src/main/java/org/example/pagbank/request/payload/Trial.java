package org.example.pagbank.request.payload;

public class Trial {

    private Integer days;

    private Boolean enabled;

    private Boolean hold_setup_fee;

    public Trial() {
    }

    public Integer getDays() {
        return days;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean getHold_setup_fee() {
        return hold_setup_fee;
    }
}
