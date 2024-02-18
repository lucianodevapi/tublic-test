package com.marketinginapp.startup.ms.user.api.dto.request;

import com.marketinginapp.startup.ms.user.utils.Constant;
import com.marketinginapp.startup.ms.user.utils.validator.TrimString;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserRequest(
        @TrimString
        @NotBlank(message = Constant.MUST_NOT_BE_NULL_OR_EMPTY)
        @Size(min = 3, max = 40,  message = Constant.MUST_BE_BETWEEN_3_AND_40_CHARACTERS)
        @JsonProperty(value = "name")
        String username,
        @TrimString
        @NotBlank(message = Constant.MUST_NOT_BE_NULL_OR_EMPTY)
        @Size(max = 50, message = Constant.MAXIMUM_LENGTH_50_CHARACTERS)
        @Email(message = Constant.MUST_BE_ONE_VALID_EMAIL)
        String email,
        @TrimString
        @NotBlank(message = Constant.MUST_NOT_BE_NULL_OR_EMPTY)
        @Size(min = 6, max = 30,  message = Constant.MUST_BE_BETWEEN_6_AND_30_CHARACTERS)
        String password,
        String profile,
        @JsonProperty("roles")
        List<String> roles
) {}