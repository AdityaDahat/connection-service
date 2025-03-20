package com.connection.api.v1.model.response;

public class ErrorMessage {
    public static final String CONNECTION_TYPE_NOT_FOUND = "Connection type does not exist for the provided ID.";
    public static final String CONNECTION_TOKEN_NOT_FOUND = "Connection Token does not exist for the provided ID.";
    public static final String CONNECTION_DEFINITION_NOT_FOUND = "Couldn't find a connection-definition for the provided ID.";
    public static final String CONNECTION_TYPE_CATEGORY_NOT_FOUND = "Couldn't find a connection-type-category for the provided ID.";
    public static final String STABLE_SOURCE_DEFINITION_COULD_NOT_BE_UPDATED = "Source Definition is stable could not be updated";
    public static final String CONNECTION_TYPE_CATEGORY_NAME_TAKEN = "The connection-type category with the same name already exists.";
    public static final String CONNECTION_NOT_FOUND = "Connection for the provided connection ID does not exist.";
    public static final String PROJECT_NOT_FOUND = "Project not found for the provided id.";
    public static final String CONNECTION_NAME_ALREADY_TAKEN = "The connection name already taken.";
    public static final String MISSING_SEARCH_AND_TYPE_IN_REQ = "Missing search and type in request.";
    public static final String INVALID_CONNECTION_TYPE = "Invalid connection type.";
    public static final String UI_CALLBACK_REQUIRED = "ui-callback is required.";

    public static final String INVALID_CONNECTION_TOKEN = "Connection token is invalid.";

}
