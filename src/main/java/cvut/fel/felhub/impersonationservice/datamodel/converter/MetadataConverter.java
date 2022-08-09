package cvut.fel.felhub.impersonationservice.datamodel.converter;

import cvut.fel.felhub.impersonationservice.datamodel.domain.Metadata;
import cvut.fel.felhub.impersonationservice.datamodel.dto.metadata.MetadataDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataConverter {

    Metadata toModel(MetadataDTO metadataDTO);

    MetadataDTO fromModel(Metadata metadata);
}
