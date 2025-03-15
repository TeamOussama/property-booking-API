package com.api.booking.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(indexes = {
        @Index(name = "idx_block_rang_dates_and_property_id", columnList = "blocked_property_id, start_date, end_date")
})
@Getter
@Setter
public class Block extends BaseEntity {

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(name = "is_archived", nullable = false)
    private boolean isArchived = false;

    @Column
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_by_id", nullable = false)
    private User blockedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_property_id")
    private Property blockedProperty;

}
