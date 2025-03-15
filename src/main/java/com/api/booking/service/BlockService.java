package com.api.booking.service;

import com.api.booking.dto.BlockDTO;
import com.api.booking.exception.NotFoundException;
import com.api.booking.mapper.BlockMapper;
import com.api.booking.repository.BlockRepository;
import com.api.booking.repository.PropertyRepository;
import com.api.booking.repository.UserRepository;
import com.api.booking.repository.entity.Block;
import jakarta.transaction.Transactional;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;


@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final BlockMapper blockMapper;
    private final LockRegistry lockRegistry;
    private final AvailabilityValidationService availabilityValidationService;

    public BlockService(final BlockRepository blockRepository, final UserRepository userRepository,
                        final PropertyRepository propertyRepository, final BlockMapper blockMapper, LockRegistry lockRegistry, AvailabilityValidationService availabilityValidationService) {
        this.blockRepository = blockRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.blockMapper = blockMapper;
        this.lockRegistry = lockRegistry;
        this.availabilityValidationService = availabilityValidationService;
    }

    public BlockDTO get(final Long id) {
        return blockRepository.findById(id)
                .map(block -> blockMapper.updateBlockDTO(block, new BlockDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final BlockDTO blockDTO) {
        final Block block = new Block();
        Lock lock = lockRegistry.obtain("LOCK_PROPERTY_" + blockDTO.getBlockedProperty());

        try {
            if (lock.tryLock()) {
                blockMapper.updateBlock(blockDTO, block, userRepository, propertyRepository);
                availabilityValidationService.validateAvailability(block.getBlockedProperty().getId(), block.getStartDate(), block.getEndDate());
                blockRepository.save(block);
            }
        } finally {
            lock.unlock();
        }


        return block.getId();
    }

    @Transactional
    public void update(final Long id, final BlockDTO blockDTO) {
        final Block block = blockRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        validateBlockNotArchived(block);
        Lock lock = lockRegistry.obtain("LOCK_PROPERTY_" + block.getBlockedProperty());

        try {
            if (lock.tryLock()) {
                blockMapper.updateBlock(blockDTO, block, userRepository, propertyRepository);
                availabilityValidationService.validateAvailability(block.getBlockedProperty().getId(), block.getStartDate(), block.getEndDate());
                blockRepository.save(block);
            }
        } finally {
            lock.unlock();
        }
    }

    public void delete(final Long id) {
        blockRepository.deleteById(id);
    }

    private void validateBlockNotArchived(Block block) {
        if (block.isArchived()) {
            throw new IllegalStateException("Cannot update archived block");
        }
    }
}
