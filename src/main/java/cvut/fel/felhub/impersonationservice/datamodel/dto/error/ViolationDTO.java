package cvut.fel.felhub.impersonationservice.datamodel.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "Violation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViolationDTO {
    private String fieldName;

    private String message;
}

