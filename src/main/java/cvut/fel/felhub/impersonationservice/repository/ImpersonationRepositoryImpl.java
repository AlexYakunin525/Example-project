package cvut.fel.felhub.impersonationservice.repository;

import cvut.fel.felhub.impersonationservice.datamodel.domain.Impersonation;
import cvut.fel.felhub.impersonationservice.service.filter.ImpersonationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ImpersonationRepositoryImpl implements ImpersonationRepositoryCustom {

    private final MongoOperations mongoOperations;

    public ImpersonationRepositoryImpl(@Autowired MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<Impersonation> filterImpersonations(ImpersonationFilter filter) {
        Query query = new Query(filter.constructCriteria());
        return mongoOperations.find(query, Impersonation.class);
    }
}
