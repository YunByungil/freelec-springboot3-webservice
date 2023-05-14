package com.jojoldu.book.freelecspringboot3webservice.web;

import com.jojoldu.book.freelecspringboot3webservice.config.auth.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = HelloController.class,
            excludeFilters = {
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
            })
class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("hello가 리턴된다")
    @Test
    @WithMockUser(roles = "USER")
    void returnHello() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @DisplayName("helloDto가 리턴된다")
    @Test
    @WithMockUser(roles = "USER")
    void returnHelloDto() throws Exception {
        String name = "hello";
        int amount = 1_000;

        mvc.perform(get("/hello/dto")
                .param("name", name) // String만 허용
                .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name))) // JSON 응답값을 필드별로 검증할 수 있는 메서드
                .andExpect(jsonPath("$.amount", is(amount))); // $를 기준으로 필드명을 명시

    }
}