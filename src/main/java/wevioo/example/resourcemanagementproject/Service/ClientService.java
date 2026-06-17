package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Config.SecurityUtils;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.Entity.Client;
import wevioo.example.resourcemanagementproject.Enums.ClientType;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.ClientRepository;
import wevioo.example.resourcemanagementproject.Mapper.ClientMapper;
import wevioo.example.resourcemanagementproject.Validator.Impl.ClientValidator;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final PaginationUtil paginationUtil;      // pour pagination
    private final ClientValidator clientValidator;
    private final SecurityUtils securityUtils;


    //  CREATE - Admin
    public ClientDTO create(ClientDTO dto) {
        securityUtils.requireAdmin();
        clientValidator.validateCreate(dto);
        Client client = clientMapper.ClientDtoToClientEntity(dto);
        Client saved = clientRepository.save(client);
        return clientMapper.ClientToClientDTO(saved);
    }

    //  UPDATE
    public ClientDTO update(Long id, ClientDTO dto) {

        securityUtils.requireAdmin();
        clientValidator.validateUpdate(dto);
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setCompany(dto.getCompany());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        existing.setTypeClient(dto.getTypeClient());
        existing.setUpdatedDate(LocalDateTime.now());

        Client updated = clientRepository.save(existing);

        return clientMapper.ClientToClientDTO(updated);
    }

    //  GET BY ID — Admin + Manager
    public ClientDTO getById(Long id) {
        securityUtils.requireAdminOrManager();
        // Géré par SecurityConfig → rien à changer ici
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        return clientMapper.ClientToClientDTO(client);
    }


    //  GET ALL — يتبدل : page تبدأ من 1  — Admin + Manager
    public PaginatedResponse<ClientDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        securityUtils.requireAdminOrManager();
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        // Manager + Admin → voient tous les clients
        // Géré par SecurityConfig → rien à changer ici
        Page<Client> clientPage = clientRepository.findAll(pageable);

        PaginatedResponse<ClientDTO> response = new PaginatedResponse<>();
        response.setContent(clientPage.getContent().stream().map(clientMapper::ClientToClientDTO).toList());
        response.setPage(clientPage.getNumber() + 1);
        response.setPageSize(clientPage.getSize());
        response.setTotalElement(clientPage.getTotalElements());
        response.setTotalPage(clientPage.getTotalPages());
        return response;
    }



    //  DELETE
    public void delete(Long id) {
        securityUtils.requireAdmin();
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        clientRepository.delete(client);
    }


    /** Retourne null si la chaîne est null ou vide après trim. */
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    //  SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<ClientDTO> searchClients(
            String name,
            String email,
            String company,
            String address,
            String phone,
            ClientType typeClient,
            Integer page,
            Integer pageSize,
            String sortBy,
            String sortDir
    ) {
        securityUtils.requireAdminOrManager();
        // ← بدل Sort.by(sortBy).ascending() مباشرة
        // نبني CustomSort ونمرروه لـ PaginationUtil بش يvalidiha
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(
                customSort,
                Sort.Direction.ASC,
                "createdDate"                  // ← default si sort == null
        );

        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Client> clientPage = clientRepository.searchClients(
                normalize(name), normalize(email), normalize(company),
                normalize(address), normalize(phone), typeClient,
                pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<ClientDTO> response = new PaginatedResponse<>();
        response.setContent(clientPage.getContent().stream()
                .map(clientMapper::ClientToClientDTO)
                .toList());
        response.setPage(clientPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(clientPage.getSize());
        response.setTotalElement(clientPage.getTotalElements());
        response.setTotalPage(clientPage.getTotalPages());

        return response;
    }


}
