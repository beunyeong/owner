package com.example.oner.repository;

import com.example.oner.entity.Member;
import com.example.oner.enums.MemberRole;
import com.example.oner.enums.MemberWait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    Optional<Object> findByUserIdAndWorkspaceIdAndWait(Long id, Long workspaceId, MemberWait memberWait);


    boolean existsByUserIdAndWorkspaceIdAndRole(Long id, Long id1, MemberRole memberRole);

}
