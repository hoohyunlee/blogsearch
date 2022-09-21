package com.hh.blogsearch.repository;

import com.hh.blogsearch.config.TestQuerydslConfig;
import com.hh.blogsearch.entity.Keyword;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.hh.blogsearch.entity.QKeyword.keyword;
/**
 * Repository의 Querydsl 쿼리 및 메소드를 위한 테스트
 */
@DataJpaTest
@Import({TestQuerydslConfig.class,KeywordQueryRepository.class})
public class KeywordRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    JPAQueryFactory jpaQueryFactory;


    @Autowired
    KeywordQueryRepository keywordQueryRepository;
    @Autowired
    KeywordRepository keywordRepository;

    // 테스트를 위해 총 11개의 더미 데이터를 사전에 저장 : (query, count) : ("q1", 1) ~ ("q11", 11)
    @BeforeEach
    void insertTestDummyData(){
        keywordRepository.saveAll(createKeywordList());
    }

    private List<Keyword> createKeywordList(){
        List<Keyword> keywordList = new ArrayList<>();
        for(int i=1; i<12; i++) {
            keywordList.add( new Keyword( i,"q"+i, i ) );
        }
        return keywordList;
    }

    @Test
    @DisplayName("count로 정렬되어 10개 리턴 : 총 11개 데이터 중 10개가 리턴되고, q11이 count:11로 가장 커서 index 0으로 리턴")
    void find10OrderByCountTest() throws Exception{

        List<Keyword> resultList = jpaQueryFactory
                .selectFrom(keyword)
                .orderBy(keyword.count.desc())
                .limit(10)
                .fetch();

        Assertions.assertEquals( 10, resultList.size() );
        Assertions.assertEquals( "q11", resultList.get(0).getQuery() );
        Assertions.assertEquals( 11, resultList.get(0).getCount() );
    }

    @Test
    @DisplayName("count로 정렬되어 10개 리턴 메소드 정상 구현 여부 : 총 11개 데이터 중 10개가 리턴되고, q11이 count:11로 가장 커서 index 0으로 리턴")
    void find10MethodTest() throws Exception{

        List<Keyword> resultList = keywordQueryRepository.find10OrderByCount();

        Assertions.assertEquals( 10, resultList.size() );
        Assertions.assertEquals( "q11", resultList.get(0).getQuery() );
        Assertions.assertEquals( 11, resultList.get(0).getCount() );
    }

    @Test
    @DisplayName("query로 count 숫자를 update 후 변경된 순위 확인 : index 1인 q10의 count에 10을 더한 후 index 0으로 리턴 됨")
    void updateByQueryTest() throws Exception{

        String q = "q10";
        long countAddValue = 10;

        long resultNum = jpaQueryFactory
                .update(keyword)
                .set(keyword.count, keyword.count.add( countAddValue ))
                .where(keyword.query.eq( q ))
                .execute();

        entityManager.flush();
        entityManager.clear();

        List<Keyword> resultList = keywordQueryRepository.find10OrderByCount();

        Assertions.assertEquals( 1, resultNum );
        Assertions.assertEquals( "q10", resultList.get(0).getQuery() );
        Assertions.assertEquals( 20, resultList.get(0).getCount() );

    }

    @Test
    @DisplayName("query로 count 숫자를 update 메소드 확인 : index 1인 q10의 count에 1을 2번 더한 후 index 0으로 리턴 됨")
    void updateByQueryMethodTest() throws Exception{

        String q = "q10";

        long resultNum = keywordQueryRepository.updateByQuery( q );
        resultNum = keywordQueryRepository.updateByQuery( q );

        entityManager.flush();
        entityManager.clear();

        List<Keyword> resultList = keywordQueryRepository.find10OrderByCount();

        Assertions.assertEquals( 1, resultNum );
        Assertions.assertEquals( "q10", resultList.get(0).getQuery() );
        Assertions.assertEquals( 12, resultList.get(0).getCount() );

    }
}
