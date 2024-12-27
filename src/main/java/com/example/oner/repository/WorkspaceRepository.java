package com.example.oner.repository;

import com.example.oner.entity.Member;
import com.example.oner.entity.User;
import com.example.oner.entity.Workspace;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    List<Workspace> findWorkspacesByUserId(Long id);

    List<Member> getMembersById(Long workspaceId);

    List<User> getUsersById(Long workspaceId);

    default Workspace findByIdOrElseThrow(Long workspaceId) {
        return findById(workspaceId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    boolean existsByIdAndUserId(Long workspaceId, Long id);

}
