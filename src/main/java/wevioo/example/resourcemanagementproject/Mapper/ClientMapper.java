package wevioo.example.resourcemanagementproject.Mapper;

import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.Entity.Client;


public class ClientMapper {
    public static ClientDTO toDTO(Client entity) {
        ClientDTO dto = new ClientDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setCompany(entity.getCompany());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setTypeClient(entity.getTypeClient());
        return dto;
    }

    public static Client toEntity(ClientDTO dto) {
        Client entity = new Client();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setCompany(dto.getCompany());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setTypeClient(dto.getTypeClient());
        return entity;
    }
}
