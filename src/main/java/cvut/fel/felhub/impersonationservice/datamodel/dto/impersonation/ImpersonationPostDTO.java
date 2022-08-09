package cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation;

import cvut.fel.felhub.impersonationservice.datamodel.dto.metadata.MetadataDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "ImpersonationPost")
@Getter
@Setter
@NoArgsConstructor
public class ImpersonationPostDTO {
    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "system identifier is mandatory")
    private String systemId;

    @Schema(defaultValue = "current date + 7 days", description = "2007-12-03T10:15:30")
    private LocalDateTime expires;

    @NotNull(message = "allowedUsernames should be not null")
    private List<String> allowedUsernames;

    @NotNull(message = "allowedRoles should be not null")
    private List<String> allowedRoles;

    @NotNull(message = "metadataList should be not null")
    @Valid
    private List<MetadataDTO> metadataList;
}
