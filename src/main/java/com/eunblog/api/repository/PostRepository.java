package com.eunblog.api.repository;

import com.eunblog.api.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long>,PostRepositoryCustom {
}
