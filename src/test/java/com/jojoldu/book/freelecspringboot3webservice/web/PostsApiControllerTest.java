package com.jojoldu.book.freelecspringboot3webservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.book.freelecspringboot3webservice.domain.posts.Posts;
import com.jojoldu.book.freelecspringboot3webservice.domain.posts.PostsRepository;
import com.jojoldu.book.freelecspringboot3webservice.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.freelecspringboot3webservice.web.dto.PostsUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1.
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @BeforeEach // 2. 매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @DisplayName("게시글 등록된다")
    @Test
    @WithMockUser(roles = "USER") // 1. 인증된 모의 사용자를 만들어서 사용, roles에 권한 추가
    void addPosts() throws Exception{
        // given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto
                .builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        // when
        mvc.perform(post(url) // 3. 생성된 MockMvc를 통해 API를 테스트 본문(Body)영역은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());


//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("게시글 수정된다")
    @Test
    @WithMockUser(roles = "USER")
    void updatePost() throws Exception {
        // given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());


//        System.out.println("\"requestEntity\" = " + "requestEntity");
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
//        System.out.println("\"222\" = " + "222");
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        System.out.println("\"333\" = " + "333");
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();

        System.out.println("\"444\" = " + "444");
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);

        System.out.println("\"555\" = " + "555");
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}