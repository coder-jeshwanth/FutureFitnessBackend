package com.FutureFitness.controller;

import com.FutureFitness.dto.request.BranchRequestDTO;
import com.FutureFitness.dto.response.BranchResponseDTO;
import com.FutureFitness.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * API endpoint to add a new branch
     *
     * @param branchRequestDTO containing branch details
     * @return ResponseEntity with BranchResponseDTO containing status information
     */
    @PostMapping("/branches")
    public ResponseEntity<BranchResponseDTO> addBranch(@RequestBody BranchRequestDTO branchRequestDTO) {
        BranchResponseDTO response = branchService.addBranch(branchRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
