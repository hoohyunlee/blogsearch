package com.hh.blogsearch.repository;

import com.hh.blogsearch.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Keyword 존재 유무 확인을 위한 JPA Repository
 */
@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    boolean existsByQuery(String Query);
}
