package com.github.mdeluise.pinboard.common;

import java.util.Collection;

public interface QueryService<E> {
    Collection<E> query(String searchString, int pageSize, int pageIndex);
}
