package cvut.fel.felhub.impersonationservice.api;

import cvut.fel.felhub.common.security.SecurityTokenConverter;
import cvut.fel.felhub.impersonationservice.datamodel.converter.ImpersonatedUserConverter;
import cvut.fel.felhub.impersonationservice.datamodel.converter.ImpersonationConverter;
import cvut.fel.felhub.impersonationservice.datamodel.converter.MetadataConverter;
import cvut.fel.felhub.impersonationservice.datamodel.domain.Impersonation;
import cvut.fel.felhub.impersonationservice.datamodel.dto.ImpersonatedUserDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.error.ErrorResponseDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation.ImpersonationResponseDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.metadata.MetadataDTO;
import cvut.fel.felhub.impersonationservice.exception.NonExistentEntityException;
import cvut.fel.felhub.impersonationservice.service.interfaces.ImpersonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Impersonation API", description = "API for using impersonation. Protected by oAuth/fake tokens.")
@RequestMapping("/impersonation")
@RestController
@Slf4j
public class ImpersonationAPI {

    private final ImpersonationService impersonationService;
    private final SecurityTokenConverter securityTokenConverter;
    private final ImpersonatedUserConverter userConverter;
    private final ImpersonationConverter impersonationConverter;
    private final MetadataConverter metadataConverter;

    public ImpersonationAPI(ImpersonationService impersonationService, ImpersonationConverter impersonationConverter, ImpersonatedUserConverter userConverter, MetadataConverter metadataConverter, SecurityTokenConverter securityTokenConverter) {
        this.impersonationService = impersonationService;
        this.impersonationConverter = impersonationConverter;
        this.userConverter = userConverter;
        this.metadataConverter = metadataConverter;
        this.securityTokenConverter = securityTokenConverter;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{systemIdentifier}/allowed", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve impersonated user data if requested impersonation is permitted. Protected with bearerAuth.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Impersonation is allowed, impersonated user data is returned.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImpersonatedUserDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Impersonation is forbidden.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Usermap person with requested impersonated username doesn't exist.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    public ImpersonatedUserDTO isImpersonationAllowed(@Autowired Authentication authentication,
                                                      @PathVariable String systemIdentifier,
                                                      @RequestParam String requestedImpersonatedUsername) {
        String username = securityTokenConverter.extractUsername(authentication);
        log.info("User with username: " + username + " is trying to impersonate user with username: " + requestedImpersonatedUsername + " in system with id: " + systemIdentifier);

        return userConverter.toModel(
                impersonationService.isImpersonationAllowed(username, systemIdentifier, requestedImpersonatedUsername));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{systemIdentifier}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve impersonation metadata. Protected with bearerAuth.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metadata is returned.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MetadataDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Impersonation data for given username and systemId doesn't exist.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            )
    })
    public List<MetadataDTO> getImpersonationMetadata(@Autowired Authentication authentication,
                                                      @PathVariable String systemIdentifier) {
        String username = securityTokenConverter.extractUsername(authentication);

        Impersonation impersonation = impersonationService.findImpersonationByUsernameAndSystemId(username, systemIdentifier)
                .orElseThrow(() -> new NonExistentEntityException("No impersonation exists for user with username: " + username + " and systemIdentifier: " + systemIdentifier));

        return impersonation.getMetadataList().stream()
                .map(metadataConverter::fromModel)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all allowed impersonations for given user. Protected with bearerAuth.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allowed impersonations are returned.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ImpersonationResponseDTO.class))
                    )
            )
    })
    public List<ImpersonationResponseDTO> getAllowedImpersonations(@Autowired Authentication authentication) {
        String username = securityTokenConverter.extractUsername(authentication);

        return impersonationService.findImpersonationsByUsername(username).stream()
                .map(impersonationConverter::fromModel)
                .collect(Collectors.toList());
    }
}
