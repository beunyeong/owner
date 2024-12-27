package com.example.oner.repository;

import com.example.oner.entity.Member;
import com.example.oner.entity.User;
import com.example.oner.entity.Workspace;
import com.example.oner.enums.MemberRole;
import com.example.oner.enums.MemberWait;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    Optional<Object> findByUserIdAndWorkspaceIdAndWait(Long id, Long workspaceId, MemberWait memberWait);


    boolean existsByUserIdAndWorkspaceIdAndRole(Long id, Long id1, MemberRole memberRole);

    // 워크스페이스 멤버 여부 확인
    boolean existsByUserAndWorkspace(User user, Workspace workspace);

    Optional<Member> findByUserAndWorkspace(User user, Workspace workspace);
    default Member findByUserAndWorkspaceOrElseThrow(User user, Workspace workspace) {
        return findByUserAndWorkspace(user, workspace).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Query("SELECT m FROM Member m JOIN FETCH m.workspace WHERE m.user = :user")
    List<Member> findAllByUser(@Param("user") User user);
    default List<Member> findByAllUserOrElseThrow(User user) {
        List<Member> members = findAllByUser(user);
        if (members.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return members;
    }




}
