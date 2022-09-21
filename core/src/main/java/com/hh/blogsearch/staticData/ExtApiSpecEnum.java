package com.hh.blogsearch.staticData;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 외부 API 호출을 위핸 URL, Param, Header 등 스택틱 Spec Value 정의
 */
@AllArgsConstructor
@Getter
public enum ExtApiSpecEnum {
    KAKAO("https://dapi.kakao.com/v2/search/blog","?query={1}&sort={2}&page={3}&size={4}",
            "accuracy","recency",
            "Authorization","KakaoAK c39917ccaa16c1bda2e7c860fd8f588f","dummy","dummy"),
    NAVER("https://openapi.naver.com/v1/search/blog","?query={1}&sort={2}&start={3}&display={4}",
            "sim","date",
            "X-Naver-Client-Id","O3AHif7po6x_Bh9eMtba","X-Naver-Client-Secret","r1yA7QZgyH");

    private final String url;
    private final String param;
    private final String sortACC;
    private final String sortREC;
    private final String HeaderK1;
    private final String HeaderV1;
    private final String HeaderK2;
    private final String HeaderV2;

}
