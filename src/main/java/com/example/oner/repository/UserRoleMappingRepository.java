package com.example.oner.repository;

import com.example.oner.entity.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {
    Optional<UserRoleMapping> findByUserIdAndResourceIdAndResourceType(Long userId, Long resourceId, String resourceType);

}

