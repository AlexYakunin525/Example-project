package cvut.fel.felhub.impersonationservice.datamodel.dto.metadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Schema(name = "Metadata")
@Getter
@Setter
@NoArgsConstructor
public class MetadataDTO {
    @NotBlank(message = "key is mandatory")
    private String key;

    @NotBlank(message = "value is mandatory")
    private String value;
}
