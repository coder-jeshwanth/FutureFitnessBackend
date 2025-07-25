package com.FutureFitness.controller;

import com.FutureFitness.dto.request.MemberRequestDTO;
import com.FutureFitness.dto.response.MemberResponseDTO;
import com.FutureFitness.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * API endpoint to add a new member
     *
     * @param memberRequestDTO containing member details
     * @return ResponseEntity with MemberResponseDTO containing status information
     */
    @PostMapping("/members")
    public ResponseEntity<MemberResponseDTO> addMember(@RequestBody MemberRequestDTO memberRequestDTO) {
        MemberResponseDTO response = memberService.addMember(memberRequestDTO);

        // Return appropriate HTTP status based on the response status code
        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }
}
