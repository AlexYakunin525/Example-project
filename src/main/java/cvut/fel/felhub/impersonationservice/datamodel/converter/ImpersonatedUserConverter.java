package cvut.fel.felhub.impersonationservice.datamodel.converter;

import cvut.fel.felhub.impersonationservice.datamodel.dto.ImpersonatedUserDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.usermap.UsermapPersonDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImpersonatedUserConverter {

    ImpersonatedUserDTO toModel(UsermapPersonDTO usermapPersonDTO);
}
