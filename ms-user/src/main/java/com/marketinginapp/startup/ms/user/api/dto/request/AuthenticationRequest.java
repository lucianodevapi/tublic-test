package com.marketinginapp.startup.ms.user.api.dto.request;

import com.marketinginapp.startup.ms.user.utils.Constant;
import com.marketinginapp.startup.ms.user.utils.validator.TrimString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @TrimString
        @NotBlank(message = Constant.MUST_NOT_BE_NULL_OR_EMPTY)
        @Size(max = 50, message = Constant.MAXIMUM_LENGTH_50_CHARACTERS)
        @Email(message = Constant.MUST_BE_ONE_VALID_EMAIL)
        String username,
        @TrimString
        @NotBlank(message = Constant.MUST_NOT_BE_NULL_OR_EMPTY)
        @Size(min = 6, max = 30,  message = Constant.MUST_BE_BETWEEN_6_AND_30_CHARACTERS)
        String password
) {}