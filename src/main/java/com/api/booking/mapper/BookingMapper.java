package com.api.booking.mapper;

import com.api.booking.dto.BookingDTO;
import com.api.booking.exception.NotFoundException;
import com.api.booking.repository.PropertyRepository;
import com.api.booking.repository.UserRepository;
import com.api.booking.repository.entity.Booking;
import com.api.booking.repository.entity.Property;
import com.api.booking.repository.entity.User;
import com.api.booking.utils.SecurityUtil;
import org.mapstruct.*;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BookingMapper {

    @Mapping(target = "bookedProperty", ignore = true)
    @Mapping(target = "guest", ignore = true)
    BookingDTO updateBookingDTO(Booking booking, @MappingTarget BookingDTO bookingDTO);

    @AfterMapping
    default void afterUpdateBookingDTO(Booking booking, @MappingTarget BookingDTO bookingDTO) {
        bookingDTO.setBookedProperty(booking.getBookedProperty() == null ? null : booking.getBookedProperty().getId());
        bookingDTO.setGuest(booking.getGuest() == null ? null : booking.getGuest().getId());
        bookingDTO.setIsCanceled(booking.isCanceled());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookedProperty", ignore = true)
    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "canceled", ignore = true)
    Booking updateBooking(BookingDTO bookingDTO, @MappingTarget Booking booking,
                          @Context PropertyRepository propertyRepository, @Context UserRepository userRepository);

    @AfterMapping
    default void afterUpdateBooking(BookingDTO bookingDTO, @MappingTarget Booking booking,
                                    @Context PropertyRepository propertyRepository,
                                    @Context UserRepository userRepository) {

        if (booking.getBookedProperty() != null && bookingDTO.getBookedProperty() != null && !bookingDTO.getBookedProperty().equals(booking.getBookedProperty().getId())) {
            throw new IllegalArgumentException("Updating a booking property after creation is not allowed. Please delete this booking and create a new one on the desired property.");
        }

        if (booking.getBookedProperty() == null && bookingDTO.getBookedProperty() == null) {
            throw new IllegalArgumentException("Booking property cannot be null.");
        }

        final Property bookedProperty = bookingDTO.getBookedProperty() == null ? null : propertyRepository.findById(bookingDTO.getBookedProperty())
                .orElseThrow(() -> new NotFoundException("bookedProperty not found"));

        if (bookedProperty != null) {
            booking.setBookedProperty(bookedProperty);
        }

        final User guest = userRepository.findById(SecurityUtil.getCurrentUser().getId())
                .orElseThrow(() -> new NotFoundException("guest not found"));
        booking.setGuest(guest);
    }

}
