package com.github.mdeluise.pinboard.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import java.util.Collection;

public interface CrudController<E, I> {
    @GetMapping
    ResponseEntity<Collection<E>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<E> find(@PathVariable I id);

    @PutMapping("/{id}")
    ResponseEntity<E> update(@Valid @RequestBody E updatedEntity, @PathVariable I id);

    @DeleteMapping("/{id}")
    void remove(@PathVariable I id);

    @PostMapping
    ResponseEntity<E> save(@Valid @RequestBody E entityToSave);
}
