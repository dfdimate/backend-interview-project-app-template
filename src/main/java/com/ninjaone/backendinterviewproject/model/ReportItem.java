package com.ninjaone.backendinterviewproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Transient;

public class ReportItem {
    private String serviceName;
    private Double serviceTotal;


    public ReportItem(String serviceName, Double serviceTotal) {
        this.serviceName = serviceName;
        this.serviceTotal = serviceTotal;
    }

    public ReportItem() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getServiceTotal() {
        return serviceTotal;
    }

    public void setServiceTotal(Double serviceTotal) {
        this.serviceTotal = serviceTotal;
    }

    @Override
    public String toString() {
        return "ReportItem{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceTotal=" + serviceTotal +
                '}';
    }
}
