package com.github.mdeluise.pinboard.page.body;

import com.github.mdeluise.pinboard.common.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PageBodyService extends AbstractCrudService<PageBody, Long> {

    @Autowired
    public PageBodyService(PageBodyRepository repository) {
        super(repository);
    }


    @Override
    @Cacheable(value = "bodies", key = "#id")
    public PageBody get(Long id) {
        return super.get(id);
    }


    @Override
    @CacheEvict(value = "bodies", key = "#id")
    public void remove(Long id) {
        super.remove(id);
    }


    @Override
    public PageBody save(PageBody entityToSave) {
        PageBody saved = super.save(entityToSave);
        saved.getPage().setPageBodyId(saved.getId());
        return saved;
    }


    @Override
    @CachePut(value = "bodies", key = "#id", unless = "#result == null")
    public PageBody update(Long id, PageBody updatedEntity) {
        return super.update(id, updatedEntity);
    }


    @Override
    protected void updateFields(PageBody toUpdate, PageBody updatedEntity) {
        toUpdate.setContent(updatedEntity.getContent());
    }
}
