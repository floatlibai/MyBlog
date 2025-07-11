package com.dev.myblog.service;

import com.dev.myblog.po.Blog;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.dev.myblog.vo.BlogQuery;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog saveBlog(Blog blog);

    Blog getBlog(Long id);

    Page<Blog> listPublishedBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listPublishedBlog(Long tagId,Pageable pageable);

    Page<Blog> listPublishedBlog(String query,Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    Blog getAndConvert(Long id);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    // for multi users
    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery, HttpSession session);

    Page<Blog> listPublishedBlog(Pageable pageable);
}
