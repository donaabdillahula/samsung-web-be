package com.dona_samsung_web_project.samsung_web_be.controller;

import com.dona_samsung_web_project.samsung_web_be.model.Member;
import com.dona_samsung_web_project.samsung_web_be.model.response.PagingResponse;
import com.dona_samsung_web_project.samsung_web_be.model.response.SuccessResponse;
import com.dona_samsung_web_project.samsung_web_be.service.MemberService;

import org.modelmapper.ModelMapper;

import jakarta.validation.Valid;

import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final ModelMapper modelMapper = new ModelMapper();

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Create or update an member
    @PostMapping
    public ResponseEntity<SuccessResponse<Member>> createOrUpdateMember(@Valid @RequestBody Member member)
            throws Exception {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Member newMember = modelMapper.map(member, Member.class);
        Member savedMember = memberService.saveMember(newMember);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>(
                        savedMember, "Success create member"));
    }

    // Get all members with pagination
    @GetMapping
    public ResponseEntity<PagingResponse<Member>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        if (page != -1 && size != -1) {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            Page<Member> members = memberService.getAllMembers(pageable);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new PagingResponse<>(
                            members, "Success get member list"));
        } else {
            Pageable unpaged = Pageable.unpaged(
                    sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            Page<Member> members = memberService.getAllMembers(unpaged);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new PagingResponse<>(
                            members, "Success get all member"));
        }
    }

    // Get member by id
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Member>> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>(
                        member, "Success get member with id " + id));
    }

    // Update an member by id
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Member>> updateMember(@PathVariable Long id,
            @Valid @RequestBody Member member) {
        Member updatedMember = memberService.updateMember(id, member);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>(
                        updatedMember, "Success update member with id " + id));
    }

    // Delete an member by id
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteMember(@PathVariable Long id) {
        memberService.getMemberById(id);
        memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new SuccessResponse<>(
                        null, "Success delete member with id " + id));
    }

    // Search members by name with pagination
    @GetMapping("/search")
    public ResponseEntity<PagingResponse<Member>> searchMembersByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Member> members = memberService.searchMembersByName(name, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PagingResponse<>(
                        members, "Success get member list"));
    }

    // Get top 5 members based on most borrowed book count
    @GetMapping("/top-borrowed")
    public ResponseEntity<PagingResponse<Member>> getTopBorrowers(
            @RequestParam(defaultValue = "5") int topN) {
        Page<Member> members = memberService.getTopMembersByBorrowedCount(topN);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PagingResponse<>(members, "Success get top members by borrowed books"));
    }
}
