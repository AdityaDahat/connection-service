package com.connection.api.v1.model.connector.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Attribute {
    private String id;
    private String name;
    private String displayName;
    private String type;
    private String dataType;
    private String format;
    private String constraint;
    private String group;
    private int length = 0;
    private Integer index;
    private Map<String, Object> properties;
    private List<String> requestCombinations = new ArrayList<>();
    private List<AssetReference> applicableFor = new ArrayList<>();
    private List<Attribute> subAttributes = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<String> getRequestCombinations() {
        return requestCombinations;
    }

    public void setRequestCombinations(List<String> requestCombinations) {
        this.requestCombinations = requestCombinations;
    }

    public List<AssetReference> getApplicableFor() {
        return applicableFor;
    }

    public void setApplicableFor(List<AssetReference> applicableFor) {
        this.applicableFor = applicableFor;
    }

    public List<Attribute> getSubAttributes() {
        return subAttributes;
    }

    public void setSubAttributes(List<Attribute> subAttributes) {
        this.subAttributes = subAttributes;
    }

    public void setName(String name) {

        if (name != null && !name.isEmpty()) {
            this.name = name.trim();
        } else {
            this.name = name;
        }
    }
}
