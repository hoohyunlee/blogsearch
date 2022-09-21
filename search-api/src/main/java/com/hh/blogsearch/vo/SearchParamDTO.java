package com.hh.blogsearch.vo;

import lombok.Data;
/**
 * Blog Search API 요청 파라미터를 위한 DTO
 */
@Data
public class SearchParamDTO {

    private String q;
    private String sort;
    private int page;
    private int size;
}
