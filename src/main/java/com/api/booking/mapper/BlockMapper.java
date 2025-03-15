package com.api.booking.mapper;

import com.api.booking.dto.BlockDTO;
import com.api.booking.exception.NotFoundException;
import com.api.booking.repository.PropertyRepository;
import com.api.booking.repository.UserRepository;
import com.api.booking.repository.entity.Block;
import com.api.booking.repository.entity.Property;
import com.api.booking.repository.entity.User;
import com.api.booking.utils.SecurityUtil;
import org.mapstruct.*;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BlockMapper {

    @Mapping(target = "blockedBy", ignore = true)
    @Mapping(target = "blockedProperty", ignore = true)
    BlockDTO updateBlockDTO(Block block, @MappingTarget BlockDTO blockDTO);

    @AfterMapping
    default void afterUpdateBlockDTO(Block block, @MappingTarget BlockDTO blockDTO) {
        blockDTO.setBlockedBy(block.getBlockedBy() == null ? null : block.getBlockedBy().getId());
        blockDTO.setBlockedProperty(block.getBlockedProperty() == null ? null : block.getBlockedProperty().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "blockedBy", ignore = true)
    @Mapping(target = "blockedProperty", ignore = true)
    Block updateBlock(BlockDTO blockDTO, @MappingTarget Block block,
                      @Context UserRepository userRepository, @Context PropertyRepository propertyRepository);

    @AfterMapping
    default void afterUpdateBlock(BlockDTO blockDTO, @MappingTarget Block block,
                                  @Context UserRepository userRepository,
                                  @Context PropertyRepository propertyRepository) {

        final User blockedBy = userRepository.findById(SecurityUtil.getCurrentUser().getId())
                .orElseThrow(() -> new NotFoundException("blockedBy user not found"));
        block.setBlockedBy(blockedBy);

        if (block.getBlockedProperty() != null && blockDTO.getBlockedProperty() != null && !blockDTO.getBlockedProperty().equals(block.getBlockedProperty().getId())) {
            throw new IllegalArgumentException("Updating a block property after creation is not allowed. Please delete this block and create a new one on the desired property.");
        }

        if (block.getBlockedProperty() == null && blockDTO.getBlockedProperty() == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }

        final Property blockedProperty = blockDTO.getBlockedProperty() == null ? null : propertyRepository.findById(blockDTO.getBlockedProperty())
                .orElseThrow(() -> new NotFoundException("blockedProperty not found"));

        if (blockedProperty != null) {
            block.setBlockedProperty(blockedProperty);
        }
    }

}
