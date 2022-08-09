package cvut.fel.felhub.impersonationservice.datamodel.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "ValidationErrorResponse")
@Getter
@Setter
public class ValidationErrorResponseDTO extends ErrorResponseDTO {

    public ValidationErrorResponseDTO() {
        super("422", "Validation errors occurred");
    }

    private final List<ViolationDTO> violations = new ArrayList<>();
}
