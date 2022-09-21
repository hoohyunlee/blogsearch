package com.hh.blogsearch.exception;

import com.hh.blogsearch.exception.vo.InvaildSearchParameterException;
import com.hh.blogsearch.exception.vo.NoAvailableExternalAPIException;
import com.hh.blogsearch.exception.vo.NoQueryParameterException;
import com.hh.blogsearch.response.CommonExceptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.Charset;

/**
 * 공통 에러 Handling 처리를 위한 ControllerAdvice
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CommonExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<CommonExceptionResponse> noParam(Exception e){
        log.error(e.getMessage());
        return new ResponseEntity<>( new CommonExceptionResponse(HttpStatus.BAD_REQUEST.value(), "필요한 파라미터가 없습니다.")
            ,getHeader() , HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler({NoQueryParameterException.class, InvaildSearchParameterException.class})
    protected ResponseEntity<CommonExceptionResponse> NoOrInvaildParameter(Exception e){
        log.error(e.getMessage());
        return new ResponseEntity<>( new CommonExceptionResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())
                ,getHeader() , HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler({NoAvailableExternalAPIException.class})
    protected ResponseEntity<CommonExceptionResponse> NoAvailableExternalAPI(NoAvailableExternalAPIException e){
        log.error(e.getMessage());
        return new ResponseEntity<>( new CommonExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())
                ,getHeader() , HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<CommonExceptionResponse> CommonException(Exception e){
        log.error(e.getMessage());
        return new ResponseEntity<>( new CommonExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())
                ,getHeader() , HttpStatus.INTERNAL_SERVER_ERROR );
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
