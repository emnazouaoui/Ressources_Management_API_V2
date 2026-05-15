package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.LeaveRequestDTO;
import wevioo.example.resourcemanagementproject.Entity.LeaveBalance;
import wevioo.example.resourcemanagementproject.Entity.LeaveRequest;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestStatus;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestType;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
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
    private final PaginationUtil paginationUtil;      // pour pagination
    private final LeavePolicyService policyService;


    // CREATE
    public LeaveRequestDTO create(LeaveRequestDTO dto) {

        LeaveRequest lr = new LeaveRequest();
        mapper.toEntity(dto, lr);

        lr.setStatus(LeaveRequestStatus.PENDING);// add for leaveBalance

        lr.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        lr.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));

        return mapper.toDTO(repository.save(lr));
    }

    // UPDATE
    public LeaveRequestDTO update(Long id, LeaveRequestDTO dto) {

        LeaveRequest lr = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest not found"));

        mapper.toEntity(dto, lr);

        lr.setUpdatedDate(LocalDateTime.now());

        if (dto.getUserId() != null) {
            lr.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        }

        if (dto.getProjectManagerId() != null) {
            lr.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));
        }

        return mapper.toDTO(repository.save(lr));
    }


    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<LeaveRequestDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<LeaveRequest> LeaveRequestPage = repository.findAll(pageable);

        PaginatedResponse<LeaveRequestDTO> response = new PaginatedResponse<>();
        response.setContent(LeaveRequestPage.getContent().stream().map(mapper::toDTO).toList());
        response.setPage(LeaveRequestPage.getNumber() + 1);
        response.setPageSize(LeaveRequestPage.getSize());
        response.setTotalElement(LeaveRequestPage.getTotalElements());
        response.setTotalPage(LeaveRequestPage.getTotalPages());
        return response;
    }


    // GET BY ID
    public LeaveRequestDTO getById(Long id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest not found"))
        );
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }


    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<LeaveRequestDTO> searchLeaveRequests(
            String reason,
            LeaveRequestType type,
            LeaveRequestStatus status,
            Long userId,
            Long projectManagerId,
            String username,
            LocalDateTime startDate,
            LocalDateTime endDate,
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

        Page<LeaveRequest> LeaveRequestPage = repository.searchLeaveRequests(
                normalize(reason),
                type,
                status,
                userId,
                projectManagerId,
                normalize(username),
                startDate,
                endDate,
                pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<LeaveRequestDTO> response = new PaginatedResponse<>();
        response.setContent(LeaveRequestPage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setPage(LeaveRequestPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(LeaveRequestPage.getSize());
        response.setTotalElement(LeaveRequestPage.getTotalElements());
        response.setTotalPage(LeaveRequestPage.getTotalPages());

        return response;
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
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest not found"));


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
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                    LeaveBalance lb = new LeaveBalance();
                    lb.setUser(user);
                    lb.setAnnualTotal(30);
                    lb.setAnnualUsed(0);

                    return leaveBalanceRepository.save(lb);
                });
    }

}
