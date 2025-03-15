package com.api.booking.controller;

import com.api.booking.dto.BookingDTO;
import com.api.booking.dto.RebookRequest;
import com.api.booking.repository.entity.UserRole;
import com.api.booking.service.BookingService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@bookingSecurityService.isGuestOrOwnerOrManager(#id, authentication.principal)")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + UserRole.Fields.GUEST + "')")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBooking(@RequestBody @Valid final BookingDTO bookingDTO) {
        final Long createdId = bookingService.create(bookingDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@bookingSecurityService.isBookingCreator(#id, authentication.principal)")
    public ResponseEntity<Long> updateBooking(@PathVariable(name = "id") final Long id,
                                              @RequestBody @Valid final BookingDTO bookingDTO) {
        bookingService.update(id, bookingDTO);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/{bookingId}/cancel")
    @PreAuthorize("@bookingSecurityService.isBookingCreator(#bookingId, authentication.principal)")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable(name = "bookingId") final Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookingId}/rebook")
    @PreAuthorize("@bookingSecurityService.isBookingCreator(#bookingId, authentication.principal)")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> rebookCanceledBooking(
            @PathVariable(name = "bookingId") final Long bookingId,
            @RequestBody @Valid final RebookRequest rebookRequest) {
        bookingService.rebookCanceledBooking(
                bookingId, rebookRequest.getStartDate(), rebookRequest.getEndDate());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@bookingSecurityService.isBookingCreator(#id, authentication.principal)")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBooking(@PathVariable(name = "id") final Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
