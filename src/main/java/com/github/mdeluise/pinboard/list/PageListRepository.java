package com.github.mdeluise.pinboard.list;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageListRepository extends JpaRepository<PageList, Long>, JpaSpecificationExecutor<PageList> {
    Optional<PageList> getByName(String name);
}
