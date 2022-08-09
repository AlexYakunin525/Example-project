package cvut.fel.felhub.impersonationservice.datamodel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

;

@Schema(name = "ImpersonatedUser")
@Getter
@Setter
@NoArgsConstructor
public class ImpersonatedUserDTO {

    private String username;

    private String firstName;

    private String lastName;

    private String fullName;

    private List<String> emails = new ArrayList<>();

    private String preferredEmail;
}
