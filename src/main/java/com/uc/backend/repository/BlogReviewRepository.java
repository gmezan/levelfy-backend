package com.uc.backend.repository;

import com.uc.backend.entity.BlogReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogReviewRepository extends JpaRepository<BlogReview, Integer> {
}
