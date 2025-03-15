package com.api.booking.controller;

import com.api.booking.dto.BlockDTO;
import com.api.booking.repository.entity.UserRole;
import com.api.booking.service.BlockService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/blocks", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRole.Fields.MANAGER + "', '" + UserRole.Fields.OWNER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class BlockController {

    private final BlockService blockService;

    public BlockController(final BlockService blockService) {
        this.blockService = blockService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@blockSecurityService.isBlockPropertyOwnerOrManager(#id, authentication.principal)")
    public ResponseEntity<BlockDTO> getBlock(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(blockService.get(id));
    }

    @PostMapping("/property/{propertyId}")
    @ApiResponse(responseCode = "201")
    @PreAuthorize("@propertySecurityService.isPropertyOwnerOrManager(#propertyId, authentication.principal)")
    public ResponseEntity<Long> createBlock(@PathVariable(name = "propertyId") final Long propertyId, @RequestBody @Valid final BlockDTO blockDTO) {
        blockDTO.setBlockedProperty(propertyId);
        final Long createdId = blockService.create(blockDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@blockSecurityService.isBlockPropertyOwnerOrManager(#id, authentication.principal)")
    public ResponseEntity<Long> updateBlock(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final BlockDTO blockDTO) {
        blockService.update(id, blockDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("@blockSecurityService.isBlockPropertyOwnerOrManager(#id, authentication.principal)")
    public ResponseEntity<Void> deleteBlock(@PathVariable(name = "id") final Long id) {
        blockService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
