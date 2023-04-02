package com.eunblog.api.repository;

import com.eunblog.api.domain.Post;
import com.eunblog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
