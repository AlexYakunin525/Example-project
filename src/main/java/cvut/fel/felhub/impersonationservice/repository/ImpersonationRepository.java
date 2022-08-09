package cvut.fel.felhub.impersonationservice.repository;

import cvut.fel.felhub.impersonationservice.datamodel.domain.Impersonation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImpersonationRepository extends MongoRepository<Impersonation, String>, ImpersonationRepositoryCustom {

    List<Impersonation> findImpersonationsByUsername(String username);

    Optional<Impersonation> findImpersonationByUsernameAndSystemId(String username, String systemId);

    Boolean existsByUsernameAndSystemId(String username, String systemId);
}
