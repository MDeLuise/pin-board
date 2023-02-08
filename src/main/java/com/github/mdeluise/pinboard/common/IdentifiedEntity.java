package com.github.mdeluise.pinboard.common;

public interface IdentifiedEntity<E> {
    E getId();

    void setId(E id);
}
