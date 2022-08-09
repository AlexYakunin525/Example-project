package cvut.fel.felhub.impersonationservice.service;

import cvut.fel.felhub.impersonationservice.client.UsermapClient;
import cvut.fel.felhub.impersonationservice.datamodel.domain.Impersonation;
import cvut.fel.felhub.impersonationservice.datamodel.dto.usermap.UsermapPersonDTO;
import cvut.fel.felhub.impersonationservice.exception.EntityAlreadyExistsException;
import cvut.fel.felhub.impersonationservice.exception.ImpersonationForbiddenException;
import cvut.fel.felhub.impersonationservice.exception.InvalidImpersonationException;
import cvut.fel.felhub.impersonationservice.exception.NonExistentEntityException;
import cvut.fel.felhub.impersonationservice.repository.ImpersonationRepository;
import cvut.fel.felhub.impersonationservice.service.filter.ImpersonationFilter;
import cvut.fel.felhub.impersonationservice.service.interfaces.ImpersonationService;
import cvut.fel.felhub.impersonationservice.util.ImpersonationUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ImpersonationServiceImpl implements ImpersonationService {

    private final ImpersonationRepository impersonationRepository;
    private final UsermapClient usermapClient;

    @Autowired
    public ImpersonationServiceImpl(ImpersonationRepository impersonationRepository, UsermapClient usermapClient) {
        this.impersonationRepository = impersonationRepository;
        this.usermapClient = usermapClient;
    }

    @Override
    public Impersonation createImpersonation(Impersonation impersonation) {
        String username = impersonation.getUsername();
        String systemId = impersonation.getSystemId();

        // call is made to check if user with provided username actually exists (if not, usermap service will return 404)
        usermapClient.getPersonByUsername(username);

        if (impersonation.getId() != null) {
            throw new InvalidImpersonationException("Impersonation id should not be provided");
        }

        if (impersonationRepository.existsByUsernameAndSystemId(username, systemId)) {
            throw new EntityAlreadyExistsException("Impersonation with username: " + username + " and systemId: " + systemId + " already exists");
        }

        // call is made to check if users with provided usernames actually exist (if not usermap service will return 404)
        impersonation.getAllowedUsernames().forEach(usermapClient::getPersonByUsername);

        if (impersonation.getExpires() == null) {
            impersonation.setExpires(ImpersonationUtil.getDefaultExpirationDate());
        } else {
            ImpersonationUtil.validateRequestedImpersonationExpirationDate(impersonation.getExpires());
        }

        return impersonationRepository.save(impersonation);
    }

    @Override
    public Impersonation patchImpersonationById(String existingImpersonationId, Impersonation impersonation) {
        Impersonation existingImpersonation = impersonationRepository.findById(existingImpersonationId)
                .orElseThrow(() -> new NonExistentEntityException("Impersonation with id: " + existingImpersonationId + " doesn't exist"));

        if (impersonation.getExpires() != null) {
            ImpersonationUtil.validateRequestedImpersonationExpirationDate(impersonation.getExpires());
            existingImpersonation.setExpires(impersonation.getExpires());
        }

        if (impersonation.getAllowedUsernames() != null) {
            // call is made to check if users with provided usernames actually exist (if not usermap service will return 404)
            impersonation.getAllowedUsernames().forEach(usermapClient::getPersonByUsername);

            existingImpersonation.setAllowedUsernames(impersonation.getAllowedUsernames());
        }

        if (impersonation.getAllowedRoles() != null) {
            existingImpersonation.setAllowedRoles(impersonation.getAllowedRoles());
        }

        if (impersonation.getMetadataList() != null) {
            existingImpersonation.setMetadataList(impersonation.getMetadataList());
        }

        return impersonationRepository.save(existingImpersonation);
    }

    @Override
    public Optional<Impersonation> findImpersonationById(String id) {
        return impersonationRepository.findById(id);
    }

    @Override
    public Optional<Impersonation> findImpersonationByUsernameAndSystemId(String username, String systemId) {
        return impersonationRepository.findImpersonationByUsernameAndSystemId(username, systemId);
    }

    @Override
    public List<Impersonation> findImpersonationsByUsername(String username) {
        return impersonationRepository.findImpersonationsByUsername(username);
    }

    @Override
    public List<Impersonation> filterImpersonations(ImpersonationFilter impersonationFilter) {
        return impersonationRepository.filterImpersonations(impersonationFilter);
    }

    @Override
    public UsermapPersonDTO isImpersonationAllowed(String username, String systemId, String requestedImpersonatedUsername) {
        UsermapPersonDTO usermapPersonDTO = usermapClient.getPersonByUsername(requestedImpersonatedUsername).getBody();

        Impersonation impersonation = impersonationRepository.findImpersonationByUsernameAndSystemId(username, systemId)
                .orElseThrow(() -> new ImpersonationForbiddenException("Rights for user with username: " + username + " and systemIdentifier: " + systemId + " to impersonate for user with username: " +
                        requestedImpersonatedUsername + " doesn't exist"));

        if (LocalDateTime.now().isAfter(impersonation.getExpires())) {
            throw new ImpersonationForbiddenException("Rights for user with username: " + username + " and systemIdentifier: " + systemId + " to impersonate for user with username: " +
                    requestedImpersonatedUsername + " have expired");
        }

        if (CollectionUtils.containsAny(impersonation.getAllowedRoles(), usermapPersonDTO.getRoles())) {
            return usermapPersonDTO;
        } else if (impersonation.getAllowedUsernames().contains(requestedImpersonatedUsername)) {
            return usermapPersonDTO;
        } else {
            throw new ImpersonationForbiddenException("Rights for user with username: " + username + " and systemIdentifier: " + systemId + " to impersonate for user with username: " +
                    requestedImpersonatedUsername + " doesn't exit");
        }
    }

    @Override
    public boolean existsById(String id) {
        return impersonationRepository.existsById(id);
    }

    @Override
    public void deleteImpersonationById(String id) {
        Impersonation impersonation = impersonationRepository.findById(id)
                .orElseThrow(() -> new NonExistentEntityException("Impersonation with id: " + id + " doesn't exist"));

        impersonationRepository.delete(impersonation);
    }
}
