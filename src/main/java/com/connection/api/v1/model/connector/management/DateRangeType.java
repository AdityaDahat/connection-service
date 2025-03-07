package com.connection.api.v1.model.connector.management;

public enum DateRangeType {
    DATE_RANGE("date-range"), MONTH_RANGE("month-range"), YEAR_RANGE("year-range");

    DateRangeType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return this.type;
    }
}
