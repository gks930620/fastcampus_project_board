package com.fastcampus.projectboard.controller;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import  static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest   slice 테스트하면 되지만, 환경설정 등 다 설정해야되서 그냥 통합테스트로


@Disabled("Spring data Rest 통합테스틑 불필요하므로 제외시킴")  //테스트 안함
@DisplayName("Data Rest-API 테스트")
@Transactional
@AutoConfigureMockMvc //mock 자동설정
@SpringBootTest
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회 ")
    @Test
    public  void test() throws Exception {
        //given
        //when
        mvc.perform(get("/api/articles") )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(print());
    }


    @DisplayName("[api] 게시글 리스트 -> 댓글 리스트 조회 ")
    @Test
    public  void  articleComments() throws Exception {
        //given
        //when
        mvc.perform(get("/api/articles/1/articleComments") )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(print());
    }

    // 특정 글의 댓글이 아니라 댓글전체 or 댓글 단건도 테스트 똑같이 가능 .



}
