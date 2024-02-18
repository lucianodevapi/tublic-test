package com.marketinginapp.startup.ms.user.utils;

public interface Constant {

    public static final String MUST_BE_ONE_VALID_EMAIL = "must be one valid email";

    public static final String MUST_NOT_BE_NULL_OR_EMPTY = "must not be null or empty";
    public static final String MAXIMUM_LENGTH_50_CHARACTERS = "maximum length 50 characters";
    public static final String MUST_BE_BETWEEN_3_AND_15_CHARACTERS = "must be between 3 and 15 characters";
    public static final String MUST_BE_BETWEEN_6_AND_30_CHARACTERS = "must be between 6 and 20 characters";
    public static final String MUST_BE_BETWEEN_3_AND_40_CHARACTERS = "must be between 3 and 40 characters";

    // INFO
    public static final String INFO_INVALID_FIELDS = "invalid field(s)";
    public static final String INFO_UNAUTHORIZED = "Unauthorized";

    // EXCEPTION
    public static final String EXCEPTION_MESSAGE = "message exception: %s";
    public static final String EXCEPTION_THE_REQUEST_CANT_ACCESS_TO_RESOURCE = "the request can't access the resources";
    public static final String EXCEPTION_ROLE_NOT_FOUND_BY_ID = "role not found by id: %s";
    public static final String EXCEPTION_ROLE_ALREADY_REGISTERED = "role already registered or invalid, role: %s";
    public static final String EXCEPTION_ERROR_WHEN_UPDATING_ROLE = "error when updating role: %s";
    public static final String EXCEPTION_USER_NOT_FOUND_BY_ID = "user not found by id: %s";
    public static final String EXCEPTION_USER_NOT_FOUND_BY_EMAIL = "user not found by email: %s";
    public static final String EXCEPTION_USER_NOT_FOUND_BY_NAME_OR_EMAIL = "user not found by name: %s or email: %s";
    public static final String EXCEPTION_USER_ALREADY_REGISTERED = "user already registered";
    public static final String EXCEPTION_ERROR_WHEN_UPDATING_USER = "error when updating error: %s";
    public static final String EXCEPTION_EMAIL_ALREADY_REGISTERED = "e-mail already registered, e-mail: %s";

    // EXCEPTION TOKEN
    public static final String EXCEPTION_TOKEN_EXPIRATION = "token expiration: %s";
    public static final String EXCEPTION_TOKEN_NOT_SUPPORTED = "Token is not support: %s";
    public static final String EXCEPTION_TOKEN_INVALID_FORMAT = "token is invalid format: %s";
    public static final String EXCEPTION_TOKEN_UNAUTHORIZED = "Unauthorized: %s";



}
