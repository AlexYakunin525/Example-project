package cvut.fel.felhub.impersonationservice.service.filter;

import lombok.Builder;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;

@Builder
public class ImpersonationFilter {
    private String id;
    private String username;
    private String systemId;
    private LocalDateTime expires;

    public Criteria constructCriteria() {
        Criteria criteria = new Criteria();

        if (id != null) {
            criteria = Criteria.where("id").is(id);
        }

        if (username != null) {
            criteria = criteria.and("username").is(username);
        }

        if (systemId != null) {
            criteria = criteria.and("systemId").is(systemId);
        }

        if (expires != null) {
            criteria = criteria.and("expires").is(expires);
        }

        return criteria;
    }
}
