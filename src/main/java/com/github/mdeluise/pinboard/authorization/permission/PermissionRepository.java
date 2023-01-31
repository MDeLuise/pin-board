package com.github.mdeluise.pinboard.authorization.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByTypeAndResourceClassNameAndResourceId(PType type,
                                                                     String resourceClassName,
                                                                     String resourceId);
}
