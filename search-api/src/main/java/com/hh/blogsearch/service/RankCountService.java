package com.hh.blogsearch.service;

import com.hh.blogsearch.vo.SearchParamDTO;
import com.hh.blogsearch.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
/**
 * Local Rank-API의 조회된 키워드 카운트를 위한 API 호출 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RankCountService {
    private final WebClient webClient;

    @Value("${interface.rankService.url}")
    private String rankSvcUrl;
    @Value("${interface.rankService.param}")
    private String rankSvcParam;

    public void requestForKeywordRankCount(SearchParamDTO searchParamDTO){
        try {
            CommonResponse commonResponse = webClient.patch()
                    .uri(rankSvcUrl + rankSvcParam,
                            searchParamDTO.getQ())
                    .retrieve()
                    .bodyToMono(CommonResponse.class)
                    .doOnError(e -> log.error("[RankCount ERROR] : 현재 카운트를 할 수 없습니다. (" + e.getMessage() + ")"))
                    .block();
        } catch (Exception e){
            log.error("[RankCount ERROR] : 키워드 카운트 불가: " + searchParamDTO.getQ());
        }
    }
}
