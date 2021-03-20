package com.uc.backend.repository;

import com.uc.backend.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository  extends JpaRepository<BlogPost, Integer> {


Optional<BlogPost> findByIdblogPost(int idBlogPost);
}
