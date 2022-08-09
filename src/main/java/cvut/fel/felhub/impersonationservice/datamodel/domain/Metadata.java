package cvut.fel.felhub.impersonationservice.datamodel.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Metadata {
    private String key;

    private String value;
}
