package com.ninjaone.backendinterviewproject.controller.responses;

public class TotalResponse {
    private Object totalAmount;

    public TotalResponse(Object totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Object getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Object totalAmount) {
        this.totalAmount = totalAmount;
    }
}
