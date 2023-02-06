package com.github.mdeluise.pinboard.authentication;

import com.github.mdeluise.pinboard.common.AbstractCrudService;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractCrudService<User, Long> {
    private final PasswordEncoder encoder;


    @Autowired
    public UserService(UserRepository repository, PasswordEncoder encoder) {
        super(repository);
        this.encoder = encoder;
    }


    public User get(String username) {
        return ((UserRepository) repository).findByUsername(username)
                                            .orElseThrow(() -> new EntityNotFoundException("username", username));
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public User get(Long id) {
        return super.get(id);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<User> getAll(int pageNo, int pageSize, String sortBy) {
        return super.getAll(pageNo, pageSize, sortBy);
    }


    public User save(String username, String plainPassword) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(plainPassword);
        return save(user);
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public User update(Long id, User updatedUser) {
        return super.update(id, updatedUser);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public void remove(Long id) {
        super.remove(id);
    }


    @Override
    public User save(User entityToSave) {
        entityToSave.setPassword(encoder.encode(entityToSave.getPassword()));
        return super.save(entityToSave);
    }


    @Override
    protected void updateFields(User toUpdate, User updatedEntity) {
        toUpdate.setUsername(updatedEntity.getUsername());
        toUpdate.setPassword(updatedEntity.getPassword());
        toUpdate.setRoles(updatedEntity.getRoles());
        toUpdate.setPermissions(updatedEntity.getPermissions());
    }


    public boolean existsByUsername(String username) {
        return ((UserRepository) repository).existsByUsername(username);
    }
}
