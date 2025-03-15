package com.api.booking.service;

import com.api.booking.dto.JwtUserDetails;
import com.api.booking.repository.BlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlockSecurityService {

    private final BlockRepository blockRepository;

    public BlockSecurityService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    /**
     * Checks if the current user is the owner of the property related to this block
     */
    @Transactional(readOnly = true)
    public boolean isBlockOwner(Long blockId, JwtUserDetails userDetails) {
        return blockRepository.findById(blockId)
                .map(block -> block.getBlockedProperty() != null &&
                        block.getBlockedProperty().getPropertyOwner() != null &&
                        block.getBlockedProperty().getPropertyOwner().getId().equals(userDetails.getId()))
                .orElse(false);
    }

    /**
     * Checks if the current user is a manager of the property related to this block
     */
    @Transactional(readOnly = true)
    public boolean isBlockManager(Long blockId, JwtUserDetails userDetails) {
        return blockRepository.findById(blockId)
                .map(block -> block.getBlockedProperty() != null &&
                        block.getBlockedProperty().getManagers() != null &&
                        block.getBlockedProperty().getManagers().stream()
                                .anyMatch(manager -> manager.getId().equals(userDetails.getId())))
                .orElse(false);
    }

    /**
     * Checks if the current user is either:
     * - The owner of the property
     * - A manager of the property
     */
    @Transactional(readOnly = true)
    public boolean isBlockPropertyOwnerOrManager(Long blockId, JwtUserDetails userDetails) {
        return isBlockOwner(blockId, userDetails) ||
                isBlockManager(blockId, userDetails);
    }


}

