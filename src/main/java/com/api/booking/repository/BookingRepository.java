package com.api.booking.repository;

import com.api.booking.repository.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Modifying
    @Query("UPDATE Booking b SET b.isArchived = true WHERE b.isArchived = false AND b.endDate < :date")
    int archiveBookingsOlderThan(@Param("date") LocalDate date);


    @Query(value = "SELECT EXISTS(SELECT 1 FROM booking b WHERE b.booked_property_id = :propertyId " +
            "AND b.start_date <= :endDate AND b.end_date >= :startDate " +
            "AND b.is_canceled = FALSE)",
            nativeQuery = true)
    boolean existsOverlappingBooking(
            @Param("propertyId") Long propertyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
