package com.connection.api.v1.payload.connector.management;

import java.util.ArrayList;
import java.util.List;

public class ConnectionTypeFilter {
    private String sourceConnectionTypeId;
    private List<String> connectionObjectTypes = new ArrayList<>();
    private List<String> categoryIds = new ArrayList<>();
    private List<String> tagIds = new ArrayList<>();
    private List<String> processTypes = new ArrayList<>();
    private String search;

    public String getSourceConnectionTypeId() {
        return sourceConnectionTypeId;
    }

    public void setSourceConnectionTypeId(String sourceConnectionTypeId) {
        this.sourceConnectionTypeId = sourceConnectionTypeId;
    }

    public List<String> getConnectionObjectTypes() {
        return connectionObjectTypes;
    }

    public void setConnectionObjectTypes(List<String> connectionObjectTypes) {
        this.connectionObjectTypes = connectionObjectTypes;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public List<String> getProcessTypes() {
        return processTypes;
    }

    public void setProcessTypes(List<String> processTypes) {
        this.processTypes = processTypes;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
