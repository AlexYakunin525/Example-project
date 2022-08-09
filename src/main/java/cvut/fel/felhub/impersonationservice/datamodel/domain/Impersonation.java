package cvut.fel.felhub.impersonationservice.datamodel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "impersonations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Impersonation {
    @Id
    private String id;

    private String username;

    private String systemId;

    private LocalDateTime expires;

    private List<String> allowedUsernames;

    private List<String> allowedRoles;

    private List<Metadata> metadataList;
}
