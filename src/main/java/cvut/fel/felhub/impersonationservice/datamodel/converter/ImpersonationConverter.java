package cvut.fel.felhub.impersonationservice.datamodel.converter;

import cvut.fel.felhub.impersonationservice.datamodel.domain.Impersonation;
import cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation.ImpersonationPostDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation.ImpersonationResponseDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.impersonation.ImpersonationUpdateDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {MetadataConverter.class})
public interface ImpersonationConverter {

    Impersonation postDTOToModel(ImpersonationPostDTO impersonationPostDTO);

    Impersonation updateDTOToModel(ImpersonationUpdateDTO impersonationUpdateDTO);

    ImpersonationResponseDTO fromModel(Impersonation impersonation);
}
