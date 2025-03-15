package com.api.booking.dto;

import com.api.booking.validator.EitherBothNullOrBothNonNull;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EitherBothNullOrBothNonNull
public class RebookRequest {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
