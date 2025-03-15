package com.api.booking.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class ValidBookingDatesValidator implements ConstraintValidator<ValidBookingDates, Object> {

    private String startDateField;
    private String endDateField;

    @Value("${booking.max.years.in.future:2}")
    private int maxYearsInFuture;

    @Override
    public void initialize(ValidBookingDates constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        LocalDate startDate = (LocalDate) beanWrapper.getPropertyValue(startDateField);
        LocalDate endDate = (LocalDate) beanWrapper.getPropertyValue(endDateField);

        // If either date is null, skip validation (handled by @NotNull if needed)
        if (startDate == null || endDate == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDate maxFutureDate = today.plusYears(maxYearsInFuture);

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        // Check if startDate is before endDate
        if (startDate.isAfter(endDate)) {
            context.buildConstraintViolationWithTemplate("Start date must be before end date")
                    .addPropertyNode(startDateField)
                    .addConstraintViolation();
            isValid = false;
        }

        // Check if startDate is after today
        if (startDate.isBefore(today)) {
            context.buildConstraintViolationWithTemplate("Start date must be today or in the future")
                    .addPropertyNode(startDateField)
                    .addConstraintViolation();
            isValid = false;
        }

        // Check if endDate is before today + maxYearsInFuture
        if (endDate.isAfter(maxFutureDate)) {
            context.buildConstraintViolationWithTemplate(
                            "End date cannot be more than " + maxYearsInFuture + " years in the future")
                    .addPropertyNode(endDateField)
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
