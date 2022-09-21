# 블로그 검색 서비스

- 이후현 (hhahoo4129@gmail.com)

---



## 1. API 명세

### 1.1. 블로그 검색 API

#### 기본 정보

```http
GET /search/blog HTTP/1.1
Host: localhost:8080
```

#### Request

##### Parameter

| Name | Type      | Description                                                  | Required |
| :--- | :-------- | :----------------------------------------------------------- | :------- |
| q    | `String`  | 검색 키워드                                                  | O        |
| sort | `String`  | 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy | X        |
| page | `Integer` | 결과 페이지 번호, 1~50 사이의 값, 기본 값 1                  | X        |
| size | `Integer` | 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10       | X        |

#### Response

##### meta

| Name          | Type      | Description      |
| :------------ | :-------- | :--------------- |
| statusCode    | `Integer` | HTTP Status Code |
| statusMessage | `String`  | 응답 결과 메시지 |

##### resultData

| Name      | Type     | Description                                                  |
| :-------- | :------- | :----------------------------------------------------------- |
| title     | `String` | 블로그 글 제목                                               |
| contents  | `String` | 블로그 글 요약                                               |
| url       | `String` | 블로그 글 URL                                                |
| blogname  | `String` | 블로그의 이름                                                |
| datetime  | `String` | 블로그 글 작성시간                                           |

#### Sample

##### Request

```bash
curl -X GET "http://localhost:8080/search/blog?q=test&sort=accuracy&page=1&size=10"
```

##### Response

```json
{
    "statusCode": 200,
    "statusMessage": "success",
    "resultData": [
        {
            "title": "[SpringBoot] <b>test</b> 코드 작성하기, Lombok",
            "contents": "@ExtendWith SpringRunner -&gt; SpringExtension @After/@Before -&gt; @AfterEach/@BeforeEach package com.talk.about.web; import org.junit.jupiter.api.<b>Test</b>; import org.junit.jupiter.api.extension.ExtendWith; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot...",
            "url": "http://jaajaa.tistory.com/253",
            "blogname": "그래도 해야지",
            "datetime": "2022-09-02T01:38:25.000+09:00"
        },
        ...
    ]
}
```



### 1.2. 인기 검색어 목록 API

#### 기본 정보

```http
GET /rank/keywords HTTP/1.1
Host: localhost:8081
```

#### Response

##### meta

| Name          | Type      | Description      |
| :------------ | :-------- | :--------------- |
| statusCode    | `Integer` | HTTP Status Code |
| statusMessage | `String`  | 응답 결과 메시지 |

##### resultData

| Name  | Type     | Description   |
| :---- | :------- | :------------ |
| id    | `long`   | Key 값        |
| query | `String` | 검색어 키워드 |
| count | `long`   | 검색 카운트   |

#### Sample

##### Request

```bash
curl -X GET "http://127.0.0.1:8081/rank/keywords"
```

##### Response

```json
{
    "statusCode": 200,
    "statusMessage": "success",
    "resultData": [
        {
            "id": 2,
            "query": "test",
            "count": 2
        }
        ...
    ]
}
```



# 2. jar파일 다운로드 링크
멀티모듈 구성으로 메인 어플리케이션은 Rank-API와 Search-API 2개로 2개 모두 실행 필요

멀티모듈 구성으로 메인 어플리케이션은 Rank-API와 Search-API 2개로, 두 어플리케이션 모두 실행 필요

- **Rank-API 경로: `github` /jars/rank-api-0.0.1-SNAPSHOT.jar**

  (https://github.com/hoohyunlee/blogsearch/blob/main/jars/rank-api-0.0.1-SNAPSHOT.jar)

  (사용포트 : 8081)

  ```bash
  java -jar rank-api-0.0.1-SNAPSHOT.jar
  ```

  

- **Search-API 경로: `github` /jars/search-api-0.0.1-SNAPSHOT.jar**

  (https://github.com/hoohyunlee/blogsearch/blob/main/jars/search-api-0.0.1-SNAPSHOT.jar)

  (사용포트: 8080, 16379 (Embedded Redis))
  
  ```bash
  java -jar search-api-0.0.1-SNAPSHOT.jar
  ```
  
  



# 3. 주요 Feature

- **Environment** : 
  - JAVA11
  - Spring Boot
  - Gradle Project
  - H2 DB
  - JPA (Querydsl : 쿼리 작성 용이)
  - Embedded Redis : 로컬 캐시 적용을 위함

- **멀티 모듈 구성**
  - **core** : 공통 응답 객체, 공통 에러 처리용 Handler, 오픈 API 호출용 Static Enum Data 관리
  - **rank-api** : 인기 검색어 목록 API 및 검색 키워드 카운트를 위한 어플리케이션
  - **search-api** : Kakao, Naver 오픈 API 호출 및 블로그 검색 결과 응답을 위한 어플리케이션
- **Kakao API**에서 정상응답을 못 받을 경우, **Naver API**를 호출하여 결과 제공
- **추가 확장성**을 위해, kakao 및 naver 등 각 오픈API 요청에 필요한 Static 데이터 (URI, Parameter명, Header 등)와 응답 받아 공용 포맷의 객체에 맵핑을 위한 응답 엘리먼트명 (kakao는 contents, naver는 description 등) 정보는 Core어플리케이션의 Enum으로 별도 관리하여 **오픈 API 추가 필요 시, 데이터만 추가 하면 됨**
- **트래픽이 많은 상황**을 대비해 블로그 검색 API는 **Embedded Redis**를 활용하여 **로컬 캐시** 적용하여 오픈 API로 요청을 최소화
- **동시성**을 위해 카운트 Update 메소드에 Transaction 구성, 향후 DB 테이블 확장 등을 위해 JPA Querydsl Repository 구성
- **테스트 케이스** : 
  - 블로그 검색 및 인기 검색어 API 통합 테스트 
  - Querydsl Select 및 Insert, Update 기능 테스트 
  - Enum 데이터 요청 및 응답을 위한 맵핑 및 Webclient 통신 테스트 







