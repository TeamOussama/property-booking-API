package com.api.booking.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;


@Entity
@Getter
@Setter
public class Property extends BaseEntity {

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_owner_id")
    private User propertyOwner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "PropertyManagers",
            joinColumns = @JoinColumn(name = "propertyId"),
            inverseJoinColumns = @JoinColumn(name = "userId"
            )
    )
    private Set<User> managers;

    @OneToMany(mappedBy = "bookedProperty", fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "blockedProperty", fetch = FetchType.LAZY)
    private Set<Block> blockedPeriods;

}
