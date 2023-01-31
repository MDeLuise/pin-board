package com.github.mdeluise.pinboard.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

public interface QueryController<E> {
    @GetMapping("/query")
    ResponseEntity<Collection<E>> query(
        @RequestParam String query,
        @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize,
        @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex
    );
}
