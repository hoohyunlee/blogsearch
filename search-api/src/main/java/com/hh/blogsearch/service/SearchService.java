package com.hh.blogsearch.service;

import com.hh.blogsearch.exception.vo.InvaildSearchParameterException;
import com.hh.blogsearch.exception.vo.NoAvailableExternalAPIException;
import com.hh.blogsearch.staticData.ExtApiResEnum;
import com.hh.blogsearch.staticData.ExtApiSpecEnum;
import com.hh.blogsearch.vo.SearchParamDTO;
import com.hh.blogsearch.vo.SearchResultVO;
import com.hh.blogsearch.exception.vo.NoQueryParameterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * KAKAO(NAVER) 블로그 검색을 위해, KAKAO(NAVER)와 통신을 위한 서비스
 * KAKAO API 에러의 경우 NAVER API로 통신
 * API 요청과 응답에 대한 Static한 스펙관련 값들은 확장성을 고려하여 Enum으로 관리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final WebClient webClient;

    public List<SearchResultVO> getBlogSearchResultSvc(SearchParamDTO searchParamDTO) {
        List<SearchResultVO> searchResultList = new ArrayList<>();

        // ExtApiSpecEnum에 등록된 외부 API 서버를 순서대로 요청하는데, 응답이 정상이면 바로 리턴 (KAKAO -> NAVER)
        for (ExtApiSpecEnum apiSpec : ExtApiSpecEnum.values()) {
            // 각 API 서버의 응답의 엘리먼트명에 대한 Static 데이터
            ExtApiResEnum apiRes = ExtApiResEnum.valueOf(apiSpec.name());
            try {
                Map resultMap = webClient.get()
                        .uri(apiSpec.getUrl() + apiSpec.getParam()
                                , searchParamDTO.getQ(), searchParamDTO.getSort().equals("accuracy") ? apiSpec.getSortACC() : apiSpec.getSortREC()
                                , searchParamDTO.getPage(), searchParamDTO.getSize())
                        .header(apiSpec.getHeaderK1(), apiSpec.getHeaderV1())
                        .header(apiSpec.getHeaderK2(), apiSpec.getHeaderV2())
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();
                // 응답받은 데이터를 SearchResultVO 형식으로 파싱하여 리턴
                List<Map<String, Object>> dataMap = (List<Map<String, Object>>) resultMap.get(apiRes.getElementName());
                for(Map<String, Object> m : dataMap){
                    searchResultList.add(new SearchResultVO(
                            m.get(apiRes.getTitleName()), m.get(apiRes.getContentsName()), m.get(apiRes.getUrlName()), m.get(apiRes.getBlognameName()), m.get(apiRes.getDatetimeName()) ));
                }
                return searchResultList;

            }catch (RuntimeException e){
                log.error(e.getMessage());
            }
        }
        throw new NoAvailableExternalAPIException();
    }
}
