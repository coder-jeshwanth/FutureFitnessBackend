package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.MemberRequestDTO;
import com.FutureFitness.dto.response.MemberResponseDTO;
import com.FutureFitness.entity.Branch;
import com.FutureFitness.entity.Member;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.BranchRepository;
import com.FutureFitness.repository.MemberRepository;
import com.FutureFitness.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, BranchRepository branchRepository) {
        this.memberRepository = memberRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public MemberResponseDTO addMember(MemberRequestDTO memberRequestDTO) {
        // Check if member with the same email already exists
        Optional<Member> existingMember = memberRepository.findByEmail(memberRequestDTO.getEmail());
        if (existingMember.isPresent()) {
            return new MemberResponseDTO(
                    "Member with this email already exists",
                    HttpStatus.CONFLICT.value()
            );
        }

        // Fetch Branch by branchId
        Branch branch = branchRepository.findById(memberRequestDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch",
                        "id",
                        memberRequestDTO.getBranchId())
                );

        // Map the request DTO to Member entity
        Member member = new Member();
        member.setName(memberRequestDTO.getName());
        member.setEmail(memberRequestDTO.getEmail());
        member.setPhone(memberRequestDTO.getPhone());
        member.setPassword(memberRequestDTO.getPassword());
        member.setBranch(branch);
        member.setCreatedAt(LocalDateTime.now());

        // Save the member
        memberRepository.save(member);

        // Return success response
        return new MemberResponseDTO(
                "Member created successfully",
                HttpStatus.CREATED.value()
        );
    }
}
