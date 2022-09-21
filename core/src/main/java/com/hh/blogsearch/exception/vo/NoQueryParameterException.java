package com.hh.blogsearch.exception.vo;

// Custom Exception : 필수 키워드 파라미터 미존재
public class NoQueryParameterException extends RuntimeException{
    public String getMessage() {
        return "q 파라미터 값이 없거나 비어있습니다.";
    }
}
