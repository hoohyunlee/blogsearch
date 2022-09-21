package com.hh.blogsearch.staticData;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 외부 API의 응답의 엘리먼트 Name 정의를 위한 Enum
 */
@Getter
@AllArgsConstructor
public enum ExtApiResEnum {
    KAKAO("documents","title","contents","url","blogname","datetime"),
    NAVER("items","title","description","bloggerlink","bloggername","postdate");

    private final String elementName;
    private final String titleName;
    private final String contentsName;
    private final String urlName;
    private final String blognameName;
    private final String datetimeName;
}
