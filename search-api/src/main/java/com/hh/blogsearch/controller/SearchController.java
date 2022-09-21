package com.hh.blogsearch.controller;

import com.hh.blogsearch.exception.vo.InvaildSearchParameterException;
import com.hh.blogsearch.exception.vo.NoQueryParameterException;
import com.hh.blogsearch.vo.SearchParamDTO;
import com.hh.blogsearch.vo.SearchResultVO;
import com.hh.blogsearch.response.CommonResponse;
import com.hh.blogsearch.service.RankCountService;
import com.hh.blogsearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.List;
/**
 * KAKAO(NAVER) 블로그 검색 결과 제공을 위한 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/search")
public class SearchController {
    private final SearchService searchService;
    private final RankCountService rankCountService;

    // KAKAO(NAVER) 블로그 검색 결과 조회를 위한 메소드
    @GetMapping("/blog")
    public ResponseEntity<CommonResponse<List<SearchResultVO>>> getBlogSearchResult(SearchParamDTO searchParamDTO){
        searchParamDTO = validCheckSearchParam(searchParamDTO);
        // 조회된 키워드는 키워드랭킹을 위해 count update를 위한 API 호출
        rankCountService.requestForKeywordRankCount(searchParamDTO);

        return new ResponseEntity<>(
                new CommonResponse<List<SearchResultVO>>( HttpStatus.OK.value(), "success",
                        searchService.getBlogSearchResultSvc(searchParamDTO) ),
                getHeader(), HttpStatus.OK);
    }

    // 요청된 블로그 검색 파라미터의 정합성 체크를 위한 메소드
    private SearchParamDTO validCheckSearchParam(SearchParamDTO searchParamDTO) {

        if(searchParamDTO.getQ() == null || searchParamDTO.getQ().isBlank()) throw new NoQueryParameterException();

        if(searchParamDTO.getSort() == null || searchParamDTO.getSort().isBlank()){
            searchParamDTO.setSort("accuracy");
        }else if(!searchParamDTO.getSort().equals("accuracy") && !searchParamDTO.getSort().equals("recency")){
            throw new InvaildSearchParameterException();
        }

        if(searchParamDTO.getPage() == 0) {
            searchParamDTO.setPage(1);
        } else if(searchParamDTO.getPage() < 1 || searchParamDTO.getPage() > 50) {
            throw new InvaildSearchParameterException();
        }

        if(searchParamDTO.getSize() == 0) {
            searchParamDTO.setSize(10);
        } else if(searchParamDTO.getSize() < 1 || searchParamDTO.getSize() > 50) {
            throw new InvaildSearchParameterException();
        }

        return searchParamDTO;
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
