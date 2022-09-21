package com.hh.blogsearch.controller;

import com.hh.blogsearch.entity.Keyword;
import com.hh.blogsearch.exception.vo.NoQueryParameterException;
import com.hh.blogsearch.response.CommonResponse;
import com.hh.blogsearch.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;
/**
 * Keyword 랭크관련 데이터 조회 및 Keyword 검색 횟수 카운터를 위한 APIs
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rank")
public class RankController {

    private final RankService rankService;

    // Keyword 조회 수 랭킹 조회 API
    @GetMapping("/keywords")
    public ResponseEntity<CommonResponse<List<Keyword>>> readKeywordRanking() {
        return new ResponseEntity<>(
            new CommonResponse<List<Keyword>>( HttpStatus.OK.value(), "success",
                    rankService.readKeywordRankingList() ),
                    getHeader(), HttpStatus.OK);
    }

    // Keyword 검색 횟수 증분을 위한 API
    @PatchMapping("/keyword")
    public ResponseEntity<CommonResponse<String>> updateKeywordCount(@RequestParam String q) {
        if(q==null || q.isBlank()) throw new NoQueryParameterException();

        rankService.updateKeywordCount(q);

        return new ResponseEntity<>(
                new CommonResponse<String>(HttpStatus.CREATED.value(), "success", "정상 등록 완료"),
                    getHeader(), HttpStatus.CREATED);
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }

}
