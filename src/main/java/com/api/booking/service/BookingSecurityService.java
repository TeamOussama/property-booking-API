package com.api.booking.service;

import com.api.booking.dto.JwtUserDetails;
import com.api.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingSecurityService {

    private final BookingRepository bookingRepository;

    public BookingSecurityService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Checks if the current user is the creator of the booking
     */
    @Transactional(readOnly = true)
    public boolean isBookingCreator(Long bookingId, JwtUserDetails userDetails) {
        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getGuest() != null &&
                        booking.getGuest().getId().equals(userDetails.getId()))
                .orElse(false);
    }

    /**
     * Checks if the current user is the owner of the property
     */
    @Transactional(readOnly = true)
    public boolean isPropertyOwner(Long bookingId, JwtUserDetails userDetails) {
        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getBookedProperty() != null &&
                        booking.getBookedProperty().getPropertyOwner() != null &&
                        booking.getBookedProperty().getPropertyOwner().getId().equals(userDetails.getId()))
                .orElse(false);
    }

    /**
     * Checks if the current user is a manager of the property
     */
    @Transactional(readOnly = true)
    public boolean isPropertyManager(Long bookingId, JwtUserDetails userDetails) {
        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getBookedProperty() != null &&
                        booking.getBookedProperty().getManagers() != null &&
                        booking.getBookedProperty().getManagers().stream()
                                .anyMatch(manager -> manager.getId().equals(userDetails.getId())))
                .orElse(false);
    }

    /**
     * Checks if the current user is either:
     * - The guest who created the booking
     * - The owner of the property
     * - A manager of the property
     */
    @Transactional(readOnly = true)
    public boolean isGuestOrOwnerOrManager(Long bookingId, JwtUserDetails userDetails) {
        return isBookingCreator(bookingId, userDetails) ||
                isPropertyOwner(bookingId, userDetails) ||
                isPropertyManager(bookingId, userDetails);
    }
}

