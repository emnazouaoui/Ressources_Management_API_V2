package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.Entity.Client;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {
    ClientDTO toDTO(Client entity);
    Client toEntity(ClientDTO dto);
}
