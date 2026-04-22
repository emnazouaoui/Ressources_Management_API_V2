package wevioo.example.resourcemanagementproject.Service;

import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.Entity.Client;
import wevioo.example.resourcemanagementproject.Mapper.ClientMapper;
import wevioo.example.resourcemanagementproject.Repository.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository repository) {
        this.clientRepository = repository;
    }

    // ✅ CREATE
    public ClientDTO create(ClientDTO dto) {
        Client client = ClientMapper.toEntity(dto);
        Client saved = clientRepository.save(client);
        return ClientMapper.toDTO(saved);
    }

    // ✅ UPDATE
    public ClientDTO update(Long id, ClientDTO dto) {

        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setCompany(dto.getCompany());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        existing.setTypeClient(dto.getTypeClient());

        Client updated = clientRepository.save(existing);

        return ClientMapper.toDTO(updated);
    }

    // ✅ GET BY ID
    public ClientDTO getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        return ClientMapper.toDTO(client);
    }

    // ✅ GET ALL
    public List<ClientDTO> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ DELETE
    public void delete(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        clientRepository.delete(client);
    }

    // ✅ SEARCH
    public List<ClientDTO> search(String keyword) {

        List<Client> clients = clientRepository.searchClients(keyword);

        return clients.stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());

    }
}
