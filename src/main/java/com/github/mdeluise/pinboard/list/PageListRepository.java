package com.github.mdeluise.pinboard.list;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PageListRepository extends JpaRepository<PageList, Long>, JpaSpecificationExecutor<PageList> {
}
