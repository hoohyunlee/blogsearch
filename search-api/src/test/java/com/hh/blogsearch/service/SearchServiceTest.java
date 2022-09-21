package com.hh.blogsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.blogsearch.staticData.ExtApiResEnum;
import com.hh.blogsearch.staticData.ExtApiSpecEnum;
import com.hh.blogsearch.vo.SearchParamDTO;
import com.hh.blogsearch.vo.SearchResultVO;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 외부 API 서비스 호출과 응답 파싱 테스트를 위한 서비스 테스트
 */
public class SearchServiceTest {
    @Autowired
    SearchService searchService;

    @Test
    @DisplayName("카카오 API 응답을 ExtApiResEnum을 통해 SearchResultVO에 정상 맵핑")
    void ExternalAPIResultResponseObjectMapping() throws Exception{
        List<SearchResultVO> searchResultList = new ArrayList<>();

        SearchParamDTO searchParamDTO = new SearchParamDTO();
        searchParamDTO.setQ("test"); searchParamDTO.setSort("accuracy"); searchParamDTO.setPage(1); searchParamDTO.setSize(1);

        ExtApiResEnum apiRes = ExtApiResEnum.valueOf("KAKAO");

        String expBody = "{\n" +
                "    \"documents\": [\n" +
                "        {\n" +
                "            \"blogname\": \"blogname1\",\n" +
                "            \"contents\": \"contents1\",\n" +
                "            \"datetime\": \"2022-09-20T15:39:00.000+09:00\",\n" +
                "            \"thumbnail\": \"thumbnail1\",\n" +
                "            \"title\": \"title1\",\n" +
                "            \"url\": \"url1\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"meta\": {\n" +
                "        \"is_end\": false,\n" +
                "        \"pageable_count\": 800,\n" +
                "        \"total_count\": 1195\n" +
                "    }\n" +
                "}";

        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setBody(String.valueOf(expBody)));
        mockWebServer.start();

        HttpUrl url = mockWebServer.url("/test");
        String str = WebClient.builder().build().get().uri(url.uri())
                .retrieve().bodyToMono(String.class).block();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = mapper.readValue(str, Map.class);

        List<Map<String, Object>> dataMap = (List<Map<String, Object>>) resultMap.get(apiRes.getElementName());
        for(Map<String, Object> m : dataMap){
            searchResultList.add(new SearchResultVO(
                    m.get(apiRes.getTitleName()), m.get(apiRes.getContentsName()), m.get(apiRes.getUrlName()), m.get(apiRes.getBlognameName()), m.get(apiRes.getDatetimeName()) ));
        }

        Assertions.assertEquals("title1",searchResultList.get(0).getTitle());
        Assertions.assertEquals("blogname1",searchResultList.get(0).getBlogname());

        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("카카오 API 요청을 ExtApiSpecEnum을 통해 정상적으로 호출")
    void ExternalAPIRequestObjectMapping() throws Exception{
        String result = null;
        SearchParamDTO searchParamDTO = new SearchParamDTO();
        searchParamDTO.setQ("test"); searchParamDTO.setSort("accuracy"); searchParamDTO.setPage(1); searchParamDTO.setSize(1);

        ExtApiSpecEnum apiSpec = ExtApiSpecEnum.valueOf("KAKAO");

        result = WebClient.builder().build().get()
                .uri(apiSpec.getUrl() + apiSpec.getParam()
                        , searchParamDTO.getQ(), searchParamDTO.getSort().equals("accuracy") ? apiSpec.getSortACC() : apiSpec.getSortREC()
                        , searchParamDTO.getPage(), searchParamDTO.getSize())
                .header(apiSpec.getHeaderK1(), apiSpec.getHeaderV1())
                .header(apiSpec.getHeaderK2(), apiSpec.getHeaderV2())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Assertions.assertNotNull(result);
    }

}
