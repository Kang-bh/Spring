package org.blog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.blog.domain.Article;
import org.blog.dto.AddArticleRequest;
import org.blog.dto.UpdateArticleRequest;
import org.blog.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // todo : 비교
//    public Optional<Article> findById(Long id) {
//
//        return Optional.ofNullable(blogRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found : " + id)));
//    }

        public Article findById(long id) {
            return blogRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        }

    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    public void deleteById(long id) {
        Article article = blogRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("not fount : " + id));

        authorizeArticleAuthor(article);
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}