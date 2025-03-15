package com.api.booking.repository;

import com.api.booking.repository.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Modifying
    @Query("UPDATE Block b SET b.isArchived = true WHERE b.isArchived = false AND b.endDate < :date")
    int archiveBlocksOlderThan(@Param("date") LocalDate date);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM block b WHERE b.blocked_property_id = :propertyId " +
            "AND b.start_date <= :endDate AND b.end_date >= :startDate)",
            nativeQuery = true)
    boolean existsOverlappingBlock(
            @Param("propertyId") Long propertyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
