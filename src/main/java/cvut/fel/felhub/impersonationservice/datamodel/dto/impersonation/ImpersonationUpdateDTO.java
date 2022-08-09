package cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation;

import cvut.fel.felhub.impersonationservice.datamodel.dto.metadata.MetadataDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "ImpersonationUpdate")
@Getter
@Setter
@NoArgsConstructor
public class ImpersonationUpdateDTO {
    private LocalDateTime expires;

    private List<String> allowedUsernames;

    private List<String> allowedRoles;

    @Valid
    private List<MetadataDTO> metadataList;
}
