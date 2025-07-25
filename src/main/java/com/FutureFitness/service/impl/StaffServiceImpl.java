package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.StaffRequestDTO;
import com.FutureFitness.dto.response.StaffResponseDTO;
import com.FutureFitness.entity.Branch;
import com.FutureFitness.entity.Role;
import com.FutureFitness.entity.Staff;
import com.FutureFitness.enums.RoleName;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.BranchRepository;
import com.FutureFitness.repository.RoleRepository;
import com.FutureFitness.repository.StaffRepository;
import com.FutureFitness.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository,
                           RoleRepository roleRepository,
                           BranchRepository branchRepository) {
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public StaffResponseDTO addStaff(StaffRequestDTO staffRequestDTO) {
        // Fetch Role by roleName (using the enum)
        RoleName roleNameEnum;
        try {
            roleNameEnum = RoleName.valueOf(staffRequestDTO.getRoleName().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Role", "name", staffRequestDTO.getRoleName());
        }

        // Find the role by name
        Role role = roleRepository.findByRoleName(roleNameEnum)
                .orElseGet(() -> {
                    // Create a new role if it doesn't exist
                    Role newRole = new Role();
                    newRole.setRoleName(roleNameEnum);
                    newRole.setCreatedAt(LocalDateTime.now());
                    return roleRepository.save(newRole);
                });

        // Fetch Branch by branchId
        Branch branch = branchRepository.findById(staffRequestDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch",
                        "id",
                        staffRequestDTO.getBranchId())
                );

        // Map the request DTO to Staff entity
        Staff staff = new Staff();
        staff.setName(staffRequestDTO.getName());
        staff.setEmail(staffRequestDTO.getEmail());
        staff.setPhone(staffRequestDTO.getPhone());
        staff.setPassword(staffRequestDTO.getPassword());
        staff.setRole(role);
        staff.setBranch(branch);
        staff.setSalary(staffRequestDTO.getSalary());
        staff.setCreatedAt(LocalDateTime.now());

        // Save the staff member
        staffRepository.save(staff);

        // Return success response
        return new StaffResponseDTO(
                "Staff created successfully",
                HttpStatus.CREATED.value()
        );
    }
}
