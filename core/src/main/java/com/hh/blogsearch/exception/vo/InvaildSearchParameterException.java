package com.hh.blogsearch.exception.vo;

// Custom Exception : 파라미터 정합성 비정상
public class InvaildSearchParameterException extends RuntimeException{
    public String getMessage() {
        return "Invaild한 파라미터로 호출하였습니다 (srot: 'accuracy' or 'recency' / page:1~50 / size:1~50)";
    }
}
