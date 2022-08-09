package cvut.fel.felhub.impersonationservice.service.interfaces;

import cvut.fel.felhub.impersonationservice.datamodel.domain.Impersonation;
import cvut.fel.felhub.impersonationservice.datamodel.dto.usermap.UsermapPersonDTO;
import cvut.fel.felhub.impersonationservice.exception.EntityAlreadyExistsException;
import cvut.fel.felhub.impersonationservice.exception.ImpersonationForbiddenException;
import cvut.fel.felhub.impersonationservice.exception.InvalidImpersonationException;
import cvut.fel.felhub.impersonationservice.exception.NonExistentEntityException;
import cvut.fel.felhub.impersonationservice.service.filter.ImpersonationFilter;

import java.util.List;
import java.util.Optional;

public interface ImpersonationService {

    /**
     * Method creates new impersonation. If provided expiration date is null then default expiration date will be used(7 days from now).
     * If not set to null expiration date will be validated, it should not be in the past and not further than 30 days in the future.
     *
     * @param impersonation impersonation
     * @throws InvalidImpersonationException if
     * 1. requested expiration date is greater than maximum expiration date
     * 2. requested expiration date is already in the past
     * 3. provided impersonation id is not set to null
     * @throws EntityAlreadyExistsException if impersonation for provided username and systemId already exists
     * @throws feign.FeignException.NotFound if user with given username doesn't exist
     * @return created impersonation
     */
    Impersonation createImpersonation(Impersonation impersonation);

    /**
     * Partially update impersonation. Only expiration date, list of allowed usernames, roles and metadata can be updated.
     *
     * @param id impersonation id
     * @param impersonation impersonation entity which contains updated data
     * @throws NonExistentEntityException in case impersonation with given id does not exist
     * @throws InvalidImpersonationException in case requested expiration date is greater than maximum expiration date or is already in the past
     * @throws feign.FeignException.NotFound if users with usernames in allowedUsernames field doesn't exist
     * @return Updated impersonation
     */
    Impersonation patchImpersonationById(String id, Impersonation impersonation);

    /**
     * Method searches for impersonation with provided id.
     *
     * @param id id of impersonation
     * @return optional of impersonation if impersonation with such id is present in the database otherwise empty optional.
     */
    Optional<Impersonation> findImpersonationById(String id);

    /**
     * Method returns all impersonation data of current user.
     *
     * @param username username
     * @return list of all impersonation
     */
    List<Impersonation> findImpersonationsByUsername(String username);

    /**
     * Searches for exact match based on impersonation filter object.
     *
     * @param impersonationFilter impersonation filter
     * @return list of matching impersonations
     */
    List<Impersonation> filterImpersonations(ImpersonationFilter impersonationFilter);

    /**
     * Method searches for impersonation with by provided username and system id.
     *
     * @param username username
     * @param systemId system identifier
     * @return optional of impersonation if impersonation with such properties is present in the database otherwise empty optional.
     */
    Optional<Impersonation> findImpersonationByUsernameAndSystemId(String username, String systemId);

    /**
     * Function finds impersonation data based on username, systemId and checks whether there exists permission
     * to impersonate into requestedImpersonatedUsername.
     * Impersonation is allowed if there is allowed username which matches requestedImpersonatedUsername
     * in impersonation data or impersonation data has at least one matching role with roles of requestedImpersonatedUser.
     *
     * @param username username of user who requested impersonation
     * @param systemId systemId
     * @param requestedImpersonatedUsername requested impersonated username
     * @throws ImpersonationForbiddenException if
     * 1. impersonation rights for given username and systemId to impersonate requested user doesn't exist
     * 2. impersonation right for given username and systemId to impersonate requested user  have expired
     * @throws feign.FeignException.NotFound if usermap person with requested impersonated username doesn't exist
     * @return UsermapPersonDTO in case impersonation is allowed
     */
    UsermapPersonDTO isImpersonationAllowed(String username, String systemId, String requestedImpersonatedUsername);

    /**
     * Method checks whether impersonation with provided id is present.
     *
     * @param id id of impersonation
     * @return true if impersonation with given id exists otherwise false.
     */
    boolean existsById(String id);

    /**
     * Method deletes impersonation with given id.
     *
     * @throws NonExistentEntityException if impersonation with given id doesn't exist
     * @param id id of page
     */
    void deleteImpersonationById(String id);
}
