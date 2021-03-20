package com.uc.backend.repository;

import com.uc.backend.entity.Blog;
import com.uc.backend.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlogRepository  extends JpaRepository<Blog, Integer> {


Optional<Blog> findByIdblogPost(int idBlogPost);
}
