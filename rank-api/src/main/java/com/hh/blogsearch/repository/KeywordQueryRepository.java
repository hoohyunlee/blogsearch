package com.hh.blogsearch.repository;

import com.hh.blogsearch.entity.Keyword;
import com.hh.blogsearch.entity.QKeyword;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hh.blogsearch.entity.QKeyword.keyword;
/**
 * Keyword 랭킹 조회 및 검색 횟수 업데이트를 위한 Querydsl Repository
 */
@Repository
@RequiredArgsConstructor
public class KeywordQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 카운터가 높은 10개 키워드 조회
    public List<Keyword> find10OrderByCount() {
        return jpaQueryFactory
                .selectFrom(keyword)
                .orderBy(keyword.count.desc())
                .limit(10)
                .fetch();
    }

    // 검색 된 키워드에 카운터 1 증가
    @Transactional
    public long updateByQuery(String q) {
        return jpaQueryFactory
                .update(keyword)
                .set(keyword.count, keyword.count.add(1))
                .where(keyword.query.eq(q))
                .execute();
    }

}
