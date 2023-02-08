package com.github.mdeluise.pinboard.common;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CrudController<E, I> {
    @GetMapping
    ResponseEntity<EntityBucket<E>> findAll(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                            @RequestParam(defaultValue = "id", required = false) String sortBy,
                                            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDir);

    @GetMapping("/{id}")
    ResponseEntity<E> find(@PathVariable I id);

    @PutMapping("/{id}")
    ResponseEntity<E> update(@Valid @RequestBody E updatedEntity, @PathVariable I id);

    @DeleteMapping("/{id}")
    void remove(@PathVariable I id);

    @PostMapping
    ResponseEntity<E> save(@Valid @RequestBody E entityToSave);
}
