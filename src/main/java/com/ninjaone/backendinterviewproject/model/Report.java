package com.ninjaone.backendinterviewproject.model;

import java.util.List;

public class Report {
    private Double totalCost;
    private List<ReportItem> reportItemList;

    public Report(Double totalCost, List<ReportItem> reportItemList) {
        this.totalCost = totalCost;
        this.reportItemList = reportItemList;
    }

    public Report() {
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<ReportItem> getReportItemList() {
        return reportItemList;
    }

    public void setReportItemList(List<ReportItem> reportItemList) {
        this.reportItemList = reportItemList;
    }
}
