package com.github.mdeluise.pinboard.common;

import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public abstract class AbstractCrudService<E, I> {
    protected final JpaRepository<E, I> repository;


    public AbstractCrudService(JpaRepository<E, I> repository) {
        this.repository = repository;
    }


    public Collection<E> getAll() {
        return repository.findAll();
    }


    public Page<E> getAll(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return repository.findAll(paging);
    }


    public E get(I id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }


    @Transactional
    public void remove(I id) {
        E toRemove = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        repository.delete(toRemove);
    }


    public E save(E entityToSave) {
        return repository.save(entityToSave);
    }


    @Transactional
    public E update(I id, E updatedEntity) {
        E toUpdate = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        updateFields(toUpdate, updatedEntity);
        return repository.save(toUpdate);
    }


    protected abstract void updateFields(E toUpdate, E updatedEntity);
}
