package com.dona_samsung_web_project.samsung_web_be.service;

import com.dona_samsung_web_project.samsung_web_be.exception.EntityExistException;
import com.dona_samsung_web_project.samsung_web_be.exception.NotFoundException;
import com.dona_samsung_web_project.samsung_web_be.model.Member;
import com.dona_samsung_web_project.samsung_web_be.repository.MemberRepository;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // Save an member
    public Member saveMember(@NotNull Member member) {
        // Jika ID null, berarti create, cukup cek existsByEmail
        if (member.getId() == null) {
            if (memberRepository.existsByEmail(member.getEmail())) {
                throw new EntityExistException("Email already exists");
            }
        } else {
            // Jika update, cek apakah ada member lain dengan email yang sama
            Member existing = memberRepository.findByEmail(member.getEmail());
            if (existing != null && !existing.getId().equals(member.getId())) {
                throw new EntityExistException("Email already exists");
            }
        }
        return memberRepository.save(member);
    }

    public Member updateMember(@NotNull Long id, Member updatedData) {
        Member existing = getMemberById(id); // Ini managed entity
        existing.setName(updatedData.getName());
        existing.setEmail(updatedData.getEmail());
        existing.setPhone(updatedData.getPhone());
        return memberRepository.save(existing);
    }

    // Get all members with pagination
    public Page<Member> getAllMembers(@NotNull Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    // Find member by id
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member with id " + id + " not found"));
    }

    // Delete an member by id
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    // Search members by name with pagination
    public Page<Member> searchMembersByName(@NotNull String name, @NotNull Pageable pageable) {
        return memberRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Member> getTopMembersByBorrowedCount(int topN) {
        Pageable pageable = PageRequest.of(0, topN);
        return memberRepository.findTopMembersByBorrowedCount(pageable);
    }
}
