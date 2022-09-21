package com.hh.blogsearch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
/**
 * Repository 테스트 외, Controller 및 서비스단 로직 확인을 위한 통합 테스트
 */
@SpringBootTest
@AutoConfigureMockMvc
public class RankApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("1.patch API로 신규 키워드 등록, 2.동일 키워드 재호출로 카운터 증가, 3. 순서 확인 용 신규 키워드 등록 4.get API로 결과 확인")
    void keywordAPIsTest() throws Exception {

        String q1 = "Query Test1";
        String q2 = "Query Test2";

        // 1. 최초 q1을 호출하여 신규로 등록
        mockMvc.perform(patch("/blogSearch/keywordRank/keyword").param("q", q1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusMessage", is("success")));

        // 2. q1을 재호출 하여, 카운터만 증가
        mockMvc.perform(patch("/blogSearch/keywordRank/keyword").param("q", q1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusMessage", is("success")));

        // 3. q2를 호출하여 신규로 등록
        mockMvc.perform(patch("/blogSearch/keywordRank/keyword").param("q", q2))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusMessage", is("success")));

        // 4. get api 호출하여 결과 확인
        mockMvc.perform(get("/blogSearch/keywordRank/keywords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMessage", is("success")))
                .andExpect(jsonPath("$.resultData", hasSize(2)))
                // 첫번째로 q1오고 두번째로 q2가 온다
                .andExpect(jsonPath("$.resultData[0].query", is(q1)))
                .andExpect(jsonPath("$.resultData[1].query", is(q2)))
                // q1의 카운트는 2이다.
                .andExpect(jsonPath("$.resultData[0].count", is(2)))
                .andDo(print());

    }
}
