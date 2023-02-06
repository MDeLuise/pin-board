package com.github.mdeluise.pinboard.page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long>, JpaSpecificationExecutor<Page> {
    org.springframework.data.domain.Page<Page> findByTitleIgnoreCaseContaining(String title, Pageable pageable);
}
