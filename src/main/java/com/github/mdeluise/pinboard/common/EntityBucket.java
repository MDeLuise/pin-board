package com.github.mdeluise.pinboard.common;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

// Created just to improve readability of entities in the Swagger UI.
// Using directly Page instead of EntityBucket result in entities named as: PagePage, PagePageList, etc.
@Tag(
    name = "EntityBucket",
    description = "Represents a bucket of entities. A bucket is intended as a pageable, sorted set."
)
public class EntityBucket<T> extends PageImpl<T> {

    public EntityBucket(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());

    }
}
