package com.api.booking.repository;

import com.api.booking.repository.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PropertyRepository extends JpaRepository<Property, Long> {
}
