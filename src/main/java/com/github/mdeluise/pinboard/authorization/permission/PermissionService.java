package com.github.mdeluise.pinboard.authorization.permission;

import com.github.mdeluise.pinboard.authentication.User;
import com.github.mdeluise.pinboard.authentication.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;


    @Autowired
    public PermissionService(PermissionRepository permissionRepository, UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }


    public Permission getOrCreate(Permission permission) {
        Optional<Permission> alreadySavedPermission =
            permissionRepository.findByTypeAndResourceClassNameAndResourceId(
                permission.getType(), permission.getResourceClass(), permission.getResourceId()
            );
        return alreadySavedPermission.orElse(permissionRepository.save(permission));
    }


    public void remove(Permission permission) {
        Hibernate.initialize(permission.getUsers());
        for (User user : permission.getUsers()) {
            user.removePermission(permission);
            userRepository.save(user);
        }
        permissionRepository.delete(permission);
    }


    public Collection<Permission> getAll() {
        return permissionRepository.findAll();
    }

}
