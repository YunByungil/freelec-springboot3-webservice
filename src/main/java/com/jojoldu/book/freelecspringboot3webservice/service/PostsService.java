package com.jojoldu.book.freelecspringboot3webservice.service;

import com.jojoldu.book.freelecspringboot3webservice.domain.posts.PostsRepository;
import com.jojoldu.book.freelecspringboot3webservice.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }
}
