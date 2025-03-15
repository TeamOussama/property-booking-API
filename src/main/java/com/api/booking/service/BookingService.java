package com.api.booking.service;

import com.api.booking.dto.BookingDTO;
import com.api.booking.exception.NotFoundException;
import com.api.booking.mapper.BookingMapper;
import com.api.booking.repository.BookingRepository;
import com.api.booking.repository.PropertyRepository;
import com.api.booking.repository.UserRepository;
import com.api.booking.repository.entity.Booking;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final LockRegistry lockRegistry;
    private final AvailabilityValidationService availabilityValidationService;

    public BookingService(final BookingRepository bookingRepository,
                          final PropertyRepository propertyRepository, final UserRepository userRepository,
                          final BookingMapper bookingMapper, LockRegistry lockRegistry, AvailabilityValidationService availabilityValidationService) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
        this.lockRegistry = lockRegistry;
        this.availabilityValidationService = availabilityValidationService;
    }

    public BookingDTO get(final Long id) {
        return bookingRepository.findById(id)
                .map(booking -> bookingMapper.updateBookingDTO(booking, new BookingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final BookingDTO bookingDTO) {
        final Booking booking = new Booking();
        Lock lock = lockRegistry.obtain("LOCK_PROPERTY_" + bookingDTO.getBookedProperty());

        try {
            if (lock.tryLock()) {
                bookingMapper.updateBooking(bookingDTO, booking, propertyRepository, userRepository);
                availabilityValidationService.validateAvailability(booking.getBookedProperty().getId(), booking.getStartDate(), booking.getEndDate());
                bookingRepository.save(booking);
            }
        } finally {
            lock.unlock();
        }

        return booking.getId();
    }

    @Transactional
    public void update(final Long id, final BookingDTO bookingDTO) {
        Lock lock = lockRegistry.obtain("LOCK_PROPERTY_" + bookingDTO.getBookedProperty());
        try {
            if (lock.tryLock()) {
                final Booking booking = bookingRepository.findById(id)
                        .orElseThrow(NotFoundException::new);
                validateBookingNotCanceledOrArchived(booking);
                bookingMapper.updateBooking(bookingDTO, booking, propertyRepository, userRepository);
                availabilityValidationService.validateAvailability(booking.getBookedProperty().getId(), booking.getStartDate(), booking.getEndDate());
                bookingRepository.save(booking);
            }
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        final Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));

        if (!booking.isCanceled()) {
            booking.setCanceled(true);
            bookingRepository.save(booking);
        }
    }

    @Transactional
    public void rebookCanceledBooking(Long bookingId, @NotNull LocalDate startDate, @NotNull LocalDate endDate) {
        final Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));

        if (!booking.isCanceled()) {
            throw new IllegalStateException("Booking is not canceled and cannot be rebooked.");
        }

        Lock lock = lockRegistry.obtain("LOCK_PROPERTY_" + booking.getBookedProperty().getId());
        try {
            if (lock.tryLock()) {
                if (startDate != null && endDate != null) {
                    booking.setStartDate(startDate);
                    booking.setEndDate(endDate);
                }

                booking.setCanceled(false);
                availabilityValidationService.validateAvailability(booking.getBookedProperty().getId(), booking.getStartDate(), booking.getEndDate());
                bookingRepository.save(booking);
            }
        } finally {
            lock.unlock();
        }
    }

    public void delete(final Long id) {
        bookingRepository.deleteById(id);
    }

    private void validateBookingNotCanceledOrArchived(Booking booking) {
        if (booking.isCanceled() || booking.isArchived()) {
            throw new IllegalStateException("Cannot update canceled booking");
        }
    }
}
