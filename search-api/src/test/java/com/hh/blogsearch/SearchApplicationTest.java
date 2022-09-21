package com.hh.blogsearch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
/**
 * Blog search API 정상 응답여부 테스트
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SearchApplicationTest
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Search blog API 수행 정상 여부 통합 테스트")
    void IntegratedAPITEST() throws Exception {

        mockMvc.perform(get("/search/blog?q=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMessage", is("success")))
                .andExpect(jsonPath("$.resultData[0].title", notNullValue()))
                .andDo(print());

    }
}
