package com.github.mdeluise.pinboard.common;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractCrudController<E extends IdentifiedEntity<I>, D, I> implements CrudController<D, I> {
    protected final AbstractCrudService<E, I> service;
    protected final AbstractDTOConverter<E, D> abstractDTOConverter;


    protected AbstractCrudController(AbstractCrudService<E, I> service,
                                     AbstractDTOConverter<E, D> abstractDTOConverter) {
        this.service = service;
        this.abstractDTOConverter = abstractDTOConverter;
    }


    @Override
    public ResponseEntity<EntityBucket<D>> findAll(Integer pageNo, Integer pageSize, String sortBy, Sort.Direction sortDir) {
        EntityBucket<D> result =
            new EntityBucket<>(service.getAll(pageNo, pageSize, sortBy, sortDir).map(abstractDTOConverter::convertToDTO));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<D> find(I id) {
        return new ResponseEntity<>(abstractDTOConverter.convertToDTO(service.get(id)), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<D> update(D updatedEntity, I id) {
        D result = abstractDTOConverter.convertToDTO(
            service.update(id, abstractDTOConverter.convertFromDTO(updatedEntity)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Override
    public void remove(I id) {
        service.remove(id);
    }


    @Override
    public ResponseEntity<D> save(D entityToSave) {
        D result = abstractDTOConverter.convertToDTO(service.save(abstractDTOConverter.convertFromDTO(entityToSave)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
