package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.LeaveRequestDTO;
import wevioo.example.resourcemanagementproject.Entity.LeaveBalance;
import wevioo.example.resourcemanagementproject.Entity.LeaveRequest;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestStatus;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestType;
import wevioo.example.resourcemanagementproject.Repository.LeaveBalanceRepository;
import wevioo.example.resourcemanagementproject.Repository.LeaveRequestRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;
import wevioo.example.resourcemanagementproject.Mapper.LeaveRequestMapper;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveRequestService {

    private final LeaveRequestRepository repository;
    private final LeaveRequestMapper mapper;
    private final UserRepository userRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeavePolicyService policyService;


    // CREATE
    public LeaveRequestDTO create(LeaveRequestDTO dto) {

        LeaveRequest lr = new LeaveRequest();
        mapper.toEntity(dto, lr);

        lr.setStatus(LeaveRequestStatus.PENDING);// add for leaveBalance

        lr.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));

        lr.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found")));

        return mapper.toDTO(repository.save(lr));
    }

    // UPDATE
    public LeaveRequestDTO update(Long id, LeaveRequestDTO dto) {

        LeaveRequest lr = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveRequest not found"));

        mapper.toEntity(dto, lr);

        if (dto.getUserId() != null) {
            lr.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }

        if (dto.getProjectManagerId() != null) {
            lr.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found")));
        }

        return mapper.toDTO(repository.save(lr));
    }

//    public LeaveRequestDTO update(Long id, LeaveRequestDTO dto) {
//
//        LeaveRequest lr = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("LeaveRequest not found"));
//
//        lr.setStartDate(dto.getStartDate());
//        lr.setEndDate(dto.getEndDate());
//        lr.setReason(dto.getReason());
//        lr.setType(dto.getType());
//
//        if (dto.getUserId() != null) {
//            lr.setUser(userRepository.findById(dto.getUserId())
//                    .orElseThrow(() -> new RuntimeException("User not found")));
//        }
//
//        if (dto.getProjectManagerId() != null) {
//            lr.setProjectManager(userRepository.findById(dto.getProjectManagerId())
//                    .orElseThrow(() -> new RuntimeException("Manager not found")));
//        }
//
//        return mapper.toDTO(repository.save(lr));
//    }

    // GET ALL
    public Page<LeaveRequestDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }


    // GET BY ID
    public LeaveRequestDTO getById(Long id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("LeaveRequest not found"))
        );
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }


    // ✅ SEARCH
    public Page<LeaveRequestDTO> searchLeaveRequests(
            String reason,
            LeaveRequestType type,
            LeaveRequestStatus status,
            Long userId,
            Long projectManagerId,
            String username,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.searchLeaveRequests(
                normalize(reason),
                type,
                status,
                userId,
                projectManagerId,
                normalize(username),
                startDate,
                endDate,
                pageable
        ).map(mapper::toDTO);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }


    //--------------------------- Update Status ---------------------//

    public LeaveRequestDTO updateStatus(Long id, LeaveRequestStatus status) {
        LeaveRequest lr = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveRequest not found"));


        if (status == LeaveRequestStatus.APPROVED) {
            validateLeave(lr);
            applyBalance(lr);
        }
        lr.setStatus(status);



        return mapper.toDTO(repository.save(lr));
    }
   //------------------------------------- Validation Type -------------------//

    private void validateLeave(LeaveRequest lr) {

        long days = java.time.temporal.ChronoUnit.DAYS.between(
                lr.getStartDate(),
                lr.getEndDate()
        ) + 1;

        //int max = policyService.maxDays(lr.getType()); if (days > max) throw ...

        switch (lr.getType()) {

            case SICK -> {
                if (days > 5)
                    throw new RuntimeException("SICK leave max is 5 days");
            }

            case PATERNITY -> {
                if (days > 2)
                    throw new RuntimeException("PATERNITY max is 2 days");
            }

            case MATERNITY -> {
                if (days > 60)
                    throw new RuntimeException("MATERNITY max is 60 days");
            }

            case ANNUAL -> {
                LeaveBalance balance = getBalance(lr.getUser().getId());

                int remaining = balance.getAnnualTotal() - balance.getAnnualUsed();

                if (days > remaining)
                    throw new RuntimeException("Not enough annual leave balance");
            }
        }
    }

    //----------------------- Apply balance ----------------------------------//

    private void applyBalance(LeaveRequest lr) {

        if (lr.getType() != LeaveRequestType.ANNUAL) return;
        long days = java.time.temporal.ChronoUnit.DAYS.between(
                lr.getStartDate(),
                lr.getEndDate()
        ) + 1;
        LeaveBalance balance = getBalance(lr.getUser().getId());

        balance.setAnnualUsed(balance.getAnnualUsed() + (int) days);

        leaveBalanceRepository.save(balance);
    }

    //------------------------ Get Balance helper method --------------------------//

    private LeaveBalance getBalance(Long userId) {
//        return leaveBalanceRepository.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        return leaveBalanceRepository.findByUserId(userId)
                .orElseGet(() -> {

                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    LeaveBalance lb = new LeaveBalance();
                    lb.setUser(user);
                    lb.setAnnualTotal(30);
                    lb.setAnnualUsed(0);

                    return leaveBalanceRepository.save(lb);
                });
    }

}
