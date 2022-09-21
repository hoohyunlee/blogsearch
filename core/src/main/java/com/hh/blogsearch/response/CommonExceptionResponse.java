package com.hh.blogsearch.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * API 공통 에러 Response
 */
@Data
@AllArgsConstructor
public class CommonExceptionResponse {
    private int errorCode;

    private String errorMessage;
}
