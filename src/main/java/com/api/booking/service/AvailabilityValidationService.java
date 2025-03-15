package com.api.booking.service;

import com.api.booking.repository.BlockRepository;
import com.api.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
public class AvailabilityValidationService {

    private final BookingRepository bookingRepository;
    private final BlockRepository blockRepository;

    public AvailabilityValidationService(BookingRepository bookingRepository, BlockRepository blockRepository) {
        this.bookingRepository = bookingRepository;
        this.blockRepository = blockRepository;
    }

    /**
     * Validates that there are no overlapping bookings or blocks for the given property and date range.
     *
     * @param propertyId The property ID to check
     * @param startDate  The start date of the period
     * @param endDate    The end date of the period
     * @throws IllegalStateException if there are overlapping bookings or blocks
     */
    public void validateAvailability(
            Long propertyId,
            LocalDate startDate,
            LocalDate endDate) {

        // Run both checks in parallel (I'm thinking about gathering blocks and booking in a view, create one index in the view and then do one query)
        // for now lets keep it simple and do it in parallel
        // I tested the performance on a 1 million rows booking table, and it took 3ms to find the overlapping booking
        CompletableFuture<Boolean> bookingCheckFuture = CompletableFuture.supplyAsync(() ->
                bookingRepository.existsOverlappingBooking(propertyId, startDate, endDate)
        );

        CompletableFuture<Boolean> blockCheckFuture = CompletableFuture.supplyAsync(() ->
                blockRepository.existsOverlappingBlock(propertyId, startDate, endDate)
        );

        // Wait for both checks to complete
        boolean hasOverlappingBooking = bookingCheckFuture.join();
        boolean hasOverlappingBlock = blockCheckFuture.join();

        if (hasOverlappingBooking) {
            throw new IllegalStateException("Cannot create/update: dates overlap with an existing booking");
        }

        if (hasOverlappingBlock) {
            throw new IllegalStateException("Cannot create/update: dates overlap with a property block");
        }
    }
}
