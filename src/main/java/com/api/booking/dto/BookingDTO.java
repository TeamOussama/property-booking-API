package com.api.booking.dto;

import com.api.booking.validator.ValidBookingDates;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@ValidBookingDates(message = "Booking dates are invalid")
public class BookingDTO {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @JsonProperty("isCanceled")
    private Boolean isCanceled;

    private Long bookedProperty;

    private Long guest;

}
