package com.hh.blogsearch.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * Blog Search API 공통 응답 포맷을 위한 객체
 */
@Data
public class SearchResultVO {

    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String datetime;

    public SearchResultVO(Object title, Object contents, Object url, Object blogname, Object datetime) {
        this.title = (String) title;
        this.contents = (String) contents;
        this.url = (String) url;
        this.blogname = (String) blogname;
        this.datetime = (String) datetime;
    }
}
