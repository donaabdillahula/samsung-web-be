package com.dona_samsung_web_project.samsung_web_be.repository;

import com.dona_samsung_web_project.samsung_web_be.model.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Custom query method for finding by name (case-insensitive)
    Page<Member> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByEmail(String email);

    Member findByEmail(String email);

    @Query("""
                SELECT m FROM Member m
                LEFT JOIN BorrowedBook bb ON bb.member = m
                GROUP BY m
                ORDER BY COUNT(bb.id) DESC
            """)
    Page<Member> findTopMembersByBorrowedCount(Pageable pageable);
}
