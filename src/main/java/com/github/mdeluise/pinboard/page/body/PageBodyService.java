package com.github.mdeluise.pinboard.page.body;

import com.github.mdeluise.pinboard.common.AbstractCrudService;
import com.github.mdeluise.pinboard.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PageBodyService extends AbstractCrudService<PageBody, Long> {

    @Autowired
    public PageBodyService(PageBodyRepository repository) {
        super(repository);
    }


    @Override
    public Collection<PageBody> getAll() {
        return ((PageBodyRepository) repository).findAll();
    }


    @Override
    @Cacheable(value = "bodies", key = "#id")
    public PageBody get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    @CacheEvict(value = "bodies", key = "#id")
    public void remove(Long id) {
        PageBody toRemove = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        repository.delete(toRemove);
    }


    @Override
    public PageBody save(PageBody entityToSave) {
        PageBody saved = repository.save(entityToSave);
        saved.getPage().setPageBodyId(saved.getId());
        return saved;
    }


    @Override
    @CachePut(value = "bodies", key = "#id", unless = "#result == null")
    public PageBody update(Long id, PageBody updatedEntity) {
        PageBody toUpdate = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        toUpdate.setContent(updatedEntity.getContent());
        return repository.save(toUpdate);
    }
}
