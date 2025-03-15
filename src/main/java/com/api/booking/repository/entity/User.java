package com.api.booking.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Entity
@Table(name = "\"User\"")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "propertyOwner", fetch = FetchType.LAZY)
    private Set<Property> ownedProperties;

    @ManyToMany(mappedBy = "managers", fetch = FetchType.LAZY)
    private Set<Property> managedProperties;

    @OneToMany(mappedBy = "blockedBy", fetch = FetchType.LAZY)
    private Set<Block> blockedProperties;

    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
    private Set<Booking> bookings;

}
