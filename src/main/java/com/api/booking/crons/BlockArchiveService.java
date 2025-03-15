package com.api.booking.crons;

import com.api.booking.repository.BlockRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BlockArchiveService {

    private final BlockRepository blockRepository;

    public BlockArchiveService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    @Transactional
    public void archiveOldBlocks() {
        LocalDate today = LocalDate.now();
        blockRepository.archiveBlocksOlderThan(today);
    }
}
