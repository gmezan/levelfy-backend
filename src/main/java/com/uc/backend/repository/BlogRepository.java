package com.uc.backend.repository;

import com.uc.backend.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository  extends JpaRepository<BlogPost, Integer> {

}
