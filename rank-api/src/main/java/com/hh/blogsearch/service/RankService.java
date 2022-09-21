package com.hh.blogsearch.service;

import com.hh.blogsearch.entity.Keyword;
import com.hh.blogsearch.repository.KeywordQueryRepository;
import com.hh.blogsearch.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Keyword 랭킹 조회 및 검색 횟수 업데이트를 위한 Service
 */
@Service
@RequiredArgsConstructor
public class RankService {

    private final KeywordRepository keywordRepository;
    private final KeywordQueryRepository keywordQueryRepository;

    // Keyword 조회 수 랭킹 조회 서비스
    public List<Keyword> readKeywordRankingList() {

        return keywordQueryRepository.find10OrderByCount();
    }

    // Keyword 신규의 경우 insert, 기등록된 Keyword의 경우 count + 1
    public void updateKeywordCount( String q ) {
        if(keywordRepository.existsByQuery( q )){
            keywordQueryRepository.updateByQuery( q );
        } else {
            Keyword kw = new Keyword();
            kw.setKeyword( q, (long) 1 );
            keywordRepository.save( kw );
        }
    }

}
