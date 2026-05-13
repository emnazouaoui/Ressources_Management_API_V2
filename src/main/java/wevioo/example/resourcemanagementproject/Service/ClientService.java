package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.Entity.Client;
import wevioo.example.resourcemanagementproject.Enums.ClientType;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.ClientRepository;
import wevioo.example.resourcemanagementproject.Mapper.ClientMapper;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final PaginationUtil paginationUtil;      // pour pagination


//    public ClientService(ClientRepository repository) {
//        this.clientRepository = repository;
//    }

    // ✅ CREATE
    public ClientDTO create(ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);
        Client saved = clientRepository.save(client);
        return clientMapper.toDTO(saved);
    }

    // ✅ UPDATE
    public ClientDTO update(Long id, ClientDTO dto) {

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

        return clientMapper.toDTO(updated);
    }

    // ✅ GET BY ID
    public ClientDTO getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        return clientMapper.toDTO(client);
    }

//    //  GET ALL — يتبدل : page تبدأ من 1
//    public Page<ClientDTO> getAll(Integer page, Integer pageSize, CustomSort sort) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//        return clientRepository.findAll(pageable).map(clientMapper::toDTO);
//    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<ClientDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Client> clientPage = clientRepository.findAll(pageable);

        PaginatedResponse<ClientDTO> response = new PaginatedResponse<>();
        response.setContent(clientPage.getContent().stream().map(clientMapper::toDTO).toList());
        response.setPage(clientPage.getNumber() + 1);
        response.setPageSize(clientPage.getSize());
        response.setTotalElement(clientPage.getTotalElements());
        response.setTotalPage(clientPage.getTotalPages());
        return response;
    }



    //  DELETE
    public void delete(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        clientRepository.delete(client);
    }

//    //  SEARCH with pagination
//    public Page<ClientDTO> searchClients(
//            String name,
//            String email,
//            String company,
//            String address,
//            String phone,
//            ClientType typeClient,
//            Integer  page,
//            Integer  pageSize,
//            CustomSort sort
//    ) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//
//        return clientRepository.searchClients(
//                        normalize(name), normalize(email), normalize(company),
//                        normalize(address), normalize(phone), typeClient,
//                        pageable
//                )
//                .map(clientMapper::toDTO);  // Page<Client> → Page<ClientDTO> directement
//    }

    /** Retourne null si la chaîne est null ou vide après trim. */
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
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
                .map(clientMapper::toDTO)
                .toList());
        response.setPage(clientPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(clientPage.getSize());
        response.setTotalElement(clientPage.getTotalElements());
        response.setTotalPage(clientPage.getTotalPages());

        return response;
    }


}
