package com.github.mdeluise.pinboard.authentication;

import com.github.mdeluise.pinboard.common.AbstractCrudService;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

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


    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public User get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    public Collection<User> getAll() {
        return ((UserRepository) repository).findAll();
    }


    public User save(String username, String plainPassword) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(plainPassword);
        return save(user);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public User update(Long id, User updatedUser) {
        User userToModify = get(id);
        userToModify.setUsername(updatedUser.getUsername());
        userToModify.setPassword(updatedUser.getPassword());
        userToModify.setRoles(updatedUser.getRoles());
        userToModify.setPermissions(updatedUser.getPermissions());
        return save(userToModify);
    }


    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public void remove(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
        repository.deleteById(id);
    }


    @Override
    public User save(User entityToSave) {
        entityToSave.setPassword(encoder.encode(entityToSave.getPassword()));
        return repository.save(entityToSave);
    }


    public boolean existsByUsername(String username) {
        return ((UserRepository) repository).existsByUsername(username);
    }
}
