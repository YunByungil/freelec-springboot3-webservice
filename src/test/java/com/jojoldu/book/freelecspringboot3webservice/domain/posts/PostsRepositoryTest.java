package com.jojoldu.book.freelecspringboot3webservice.domain.posts;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest // 별다른 설정 없이 @SpringBootTest를 사용할 경우 H2 데이터베이스를 자동으로 실행
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    /*
    단위 테스트가 끝날 때마다 수행되는 메서드를 지정,
    배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용,
    여러 테스트가 동시에 수행되면 테스트용 데이터베이스인 H2에 데이터가 그대로 남아 있어 다음 테스트 실행 시 테스트가 실패할 수 있다.
     */
    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @DisplayName("게시글저장 불러오기")
    @Test
    void getPostsSave() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder() // 테이블 posts에 insert/update 쿼리를 실행, id 값이 있다면 update, 없으면 insert
                .title(title)
                .content(content)
                .author("jojoldu@gmail.com")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll(); // 테이블 posts에 있는 모든 데이터를 조회해오는 메서드

        // then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @DisplayName("BaseTimeEntity 등록")
    @Test
    void addBaseTimeEntity() {
        // given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>> createDate = " + posts.getCreatedDate() + ", modifiedDate = " + posts.getModifiedDate());
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

    @DisplayName("게시글 수정하기")
    @Test
    void updatePosts() {
        // given
        Posts id = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        Posts findPosts = postsRepository.findById(id.getId()).get();

        // when
        findPosts.update("title2", "content2");

        // then
        assertThat(findPosts.getTitle()).isEqualTo("title2");
    }

}