package com.hh.blogsearch.exception.vo;

// Custom Exception : 외부 API 연결 실패 오류
public class NoAvailableExternalAPIException extends RuntimeException{
    public String getMessage() {
        return "현재 연결 가능한 외부 API 서버가 없습니다.";
    }
}
