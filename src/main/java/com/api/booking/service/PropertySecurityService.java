package com.api.booking.service;


import com.api.booking.dto.JwtUserDetails;
import com.api.booking.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertySecurityService {

    private final PropertyRepository propertyRepository;

    public PropertySecurityService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    /**
     * Checks if the current user is the owner of the property.
     */
    @Transactional(readOnly = true)
    public boolean isPropertyOwner(Long propertyId, JwtUserDetails userDetails) {
        return propertyRepository.findById(propertyId)
                .map(property -> property.getPropertyOwner() != null &&
                        property.getPropertyOwner().getId().equals(userDetails.getId()))
                .orElse(false);
    }

    /**
     * Checks if the current user is a manager of the property.
     */
    @Transactional(readOnly = true)
    public boolean isPropertyManager(Long propertyId, JwtUserDetails userDetails) {
        return propertyRepository.findById(propertyId)
                .map(property -> property.getManagers() != null &&
                        property.getManagers().stream()
                                .anyMatch(manager -> manager.getId().equals(userDetails.getId())))
                .orElse(false);
    }

    /**
     * Checks if the current user is either:
     * - The owner of the property
     * - A manager of the property
     */
    @Transactional(readOnly = true)
    public boolean isPropertyOwnerOrManager(Long propertyId, JwtUserDetails userDetails) {
        return isPropertyOwner(propertyId, userDetails) ||
                isPropertyManager(propertyId, userDetails);
    }
}
