package com.api.booking.validator;

import com.api.booking.dto.RebookRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EitherBothNullOrBothNonNullValidator
        implements ConstraintValidator<EitherBothNullOrBothNonNull, RebookRequest> {

    @Override
    public boolean isValid(RebookRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        // Check if both fields are null or both fields are non-null
        boolean isStartDateNull = request.getStartDate() == null;
        boolean isEndDateNull = request.getEndDate() == null;

        return (isStartDateNull && isEndDateNull) || (!isStartDateNull && !isEndDateNull);
    }
}
