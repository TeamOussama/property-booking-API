package com.api.booking.dto;

import com.api.booking.repository.entity.UserRole;
import com.api.booking.validator.RegistrationRequestUsernameUnique;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationRequest {

    @NotNull
    @Size(max = 255)
    @RegistrationRequestUsernameUnique
    private String username;

    @NotNull
    @Size(max = 72)
    private String password;

    @NotNull
    private UserRole role;

}
