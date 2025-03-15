package com.api.booking.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(indexes = {
        @Index(name = "idx_booking_rang_dates_and_property_id", columnList = "booked_property_id, start_date, end_date")
})
@Getter
@Setter
public class Booking extends BaseEntity {

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column
    private boolean isCanceled;

    @Column(name = "is_archived", nullable = false)
    private boolean isArchived = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booked_property_id")
    private Property bookedProperty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private User guest;

}
