package cvut.fel.felhub.impersonationservice.api;

import cvut.fel.felhub.impersonationservice.datamodel.converter.ImpersonationConverter;
import cvut.fel.felhub.impersonationservice.datamodel.domain.Impersonation;
import cvut.fel.felhub.impersonationservice.datamodel.dto.error.ErrorResponseDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation.ImpersonationPostDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation.ImpersonationResponseDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation.ImpersonationUpdateDTO;
import cvut.fel.felhub.impersonationservice.exception.NonExistentEntityException;
import cvut.fel.felhub.impersonationservice.service.filter.ImpersonationFilter;
import cvut.fel.felhub.impersonationservice.service.interfaces.ImpersonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Management API", description = "API for creating, updating and deleting impersonation rights. Protected by basicAuth, accessible only for management stuff.")
@RequestMapping("/management")
@RestController
public class ManagementAPI {

    private final ImpersonationService impersonationService;
    private final ImpersonationConverter impersonationConverter;

    @Autowired
    public ManagementAPI(ImpersonationService impersonationService, ImpersonationConverter impersonationConverter) {
        this.impersonationService = impersonationService;
        this.impersonationConverter = impersonationConverter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/impersonation", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new impersonation rights for given user and systemId. If provided expiration date is set to null ," +
            "impersonation will expire in 7 days. Maximum possible number of days before expiration is equal to 30. Protected with basicAuth.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Impersonation rights created successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImpersonationResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Usermap persona with given username doesn't exist.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Impersonation for given username and systemId already exists.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "422", description = "Invalid impersonation data.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    public ImpersonationResponseDTO createImpersonation(@RequestBody @Valid ImpersonationPostDTO impersonationDTO) {
        Impersonation impersonation = impersonationConverter.postDTOToModel(impersonationDTO);

        return impersonationConverter.fromModel(
                impersonationService.createImpersonation(impersonation));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/impersonation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Partially update existing impersonation. Only expirationDate, usernames, roles and metadata could be updated. Protected with basicAuth.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Impersonation successfully updated..",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImpersonationResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Impersonation for given username and systemId doesn't exists or users with usernames in allowedUsernames field doesn't exist.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "422", description = "Invalid impersonation data.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    public ImpersonationResponseDTO updateImpersonation(@PathVariable String id,
                                                        @RequestBody @Valid ImpersonationUpdateDTO impersonationDTO) {
        Impersonation updatedImpersonation = impersonationService.patchImpersonationById(id, impersonationConverter.updateDTOToModel(impersonationDTO));
        return impersonationConverter.fromModel(updatedImpersonation);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/impersonation", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve impersonations based on filter parameters. Filter parameters are optional, if they are not provided all impersonation will be retrieved. Protected with basicAuth.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Impersonations successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ImpersonationResponseDTO.class))
                    )
            )
    })
    public List<ImpersonationResponseDTO> getImpersonationsByFilterParams(@RequestParam(required = false) String username,
                                                                          @RequestParam(required = false) String systemId) {
        ImpersonationFilter impersonationFilter = ImpersonationFilter.builder()
                .username(username)
                .systemId(systemId)
                .build();

        return impersonationService.filterImpersonations(impersonationFilter).stream()
                .map(impersonationConverter::fromModel)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/impersonation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve impersonation based on given id. Protected with basicAuth.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Impersonations successfully retrieved.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ImpersonationResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Impersonation for given id doesn't exist.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            )
    })
    public ImpersonationResponseDTO getImpersonationById(@PathVariable(name = "id") String impersonationId) {
        Impersonation impersonation = impersonationService.findImpersonationById(impersonationId)
                .orElseThrow(() -> new NonExistentEntityException("Impersonation with id: " + impersonationId + " doesn't exist"));

        return impersonationConverter.fromModel(impersonation);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/impersonation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete impersonation based on given id. Protected with basicAuth.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Impersonation successfully deleted.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ImpersonationResponseDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Impersonation for given id doesn't exist.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ErrorResponseDTO.class))
                    )
            )
    })
    public void deleteImpersonationById(@PathVariable String id) {
        impersonationService.deleteImpersonationById(id);
    }
}
