package com.github.mdeluise.pinboard.common;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.transaction.Transactional;
import java.util.Collection;

public abstract class AbstractCrudService<E, I> {
    protected final CrudRepository<E, I> repository;


    public AbstractCrudService(CrudRepository<E, I> repository) {
        this.repository = repository;
    }


    public abstract Collection<E> getAll();


    public abstract E get(I id);


    @Transactional
    public abstract void remove(I id);


    public abstract E save(E entityToSave);


    @Transactional
    public abstract E update(I id, E updatedEntity);


    protected Authentication getAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }
}
