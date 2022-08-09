package cvut.fel.felhub.impersonationservice.datamodel.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(name = "ErrorResponse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private final LocalDateTime timestamp = LocalDateTime.now();

    private String httpStatus;

    private String message;
}
