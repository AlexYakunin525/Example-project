package cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "ImpersonationResponse")
@Getter
@Setter
@NoArgsConstructor
public class ImpersonationResponseDTO extends ImpersonationPostDTO {
    private String id;
}
