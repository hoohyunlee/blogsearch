package com.hh.blogsearch.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 공통 응답 Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {

    private int statusCode;

    private String statusMessage;

    private T resultData;

}
