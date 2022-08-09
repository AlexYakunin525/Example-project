package cvut.fel.felhub.impersonationservice.repository;

import cvut.fel.felhub.impersonationservice.datamodel.domain.Impersonation;
import cvut.fel.felhub.impersonationservice.service.filter.ImpersonationFilter;

import java.util.List;

public interface ImpersonationRepositoryCustom {

    List<Impersonation> filterImpersonations(ImpersonationFilter filter);
}
