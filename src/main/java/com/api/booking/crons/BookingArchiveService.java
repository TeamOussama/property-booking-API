package com.api.booking.crons;

import com.api.booking.repository.BookingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BookingArchiveService {

    private final BookingRepository bookingRepository;

    public BookingArchiveService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    @Transactional
    public void archiveOldBookings() {
        LocalDate today = LocalDate.now();
        bookingRepository.archiveBookingsOlderThan(today);
    }
}

