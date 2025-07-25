package com.FutureFitness.controller;

import com.FutureFitness.dto.request.StaffRequestDTO;
import com.FutureFitness.dto.response.StaffResponseDTO;
import com.FutureFitness.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    /**
     * API endpoint to add a new staff member
     *
     * @param staffRequestDTO containing staff details
     * @return ResponseEntity with StaffResponseDTO containing status information
     */
    @PostMapping("/staff")
    public ResponseEntity<StaffResponseDTO> addStaff(@RequestBody StaffRequestDTO staffRequestDTO) {
        StaffResponseDTO response = staffService.addStaff(staffRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
