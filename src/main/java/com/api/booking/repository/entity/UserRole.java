package com.api.booking.repository.entity;

import lombok.experimental.FieldNameConstants;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum UserRole {

    @FieldNameConstants.Include
    GUEST,
    @FieldNameConstants.Include
    MANAGER,
    @FieldNameConstants.Include
    OWNER

}
