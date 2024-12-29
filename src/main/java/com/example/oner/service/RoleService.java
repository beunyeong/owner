package com.example.oner.service;

import com.example.oner.entity.User;
import com.example.oner.entity.UserRoleMapping;
import com.example.oner.enums.MemberRole;
import com.example.oner.enums.UserStatus;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import com.example.oner.repository.UserRoleMappingRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final UserRoleMappingRepository userRoleMappingRepository;

    public RoleService(UserRoleMappingRepository userRoleMappingRepository) {
        this.userRoleMappingRepository = userRoleMappingRepository;
    }

    // 1. 사용자에게 권한 부여
    public void assignRole(User user, MemberRole memberRole, Long resourceId, String resourceType) {
        if (user.getUserStatus() == UserStatus.DEACTIVATED) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DEACTIVATED);
        }

        UserRoleMapping mapping = new UserRoleMapping(user, memberRole, resourceId, resourceType);
        userRoleMappingRepository.save(mapping);
    }

    // 2. 권한 확인
    public void validatePermission(User user, MemberRole memberRole, Long resourceId, String resourceType ) {
        UserRoleMapping mapping = userRoleMappingRepository.findByUserIdAndResourceIdAndResourceType(
                        user.getId(), resourceId, resourceType)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_ACCESS));
    }
}

