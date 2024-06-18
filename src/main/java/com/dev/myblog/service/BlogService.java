package com.dev.myblog.service;

import com.dev.myblog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vo.BlogQuery;

public interface BlogService {
    Blog saveBlog(Blog blog);

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

//    Page<Blog> listBlog(Pageable pageable);
//
//    Page<Blog> listBlog(Long tagId,Pageable pageable);
//
//    Page<Blog> listBlog(String query,Pageable pageable);
//
//    List<Blog> listRecommendBlogTop(Integer size);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

//    Blog getAndConvert(Long id);
//
//    Map<String,List<Blog>> archiveBlog();
//
//    Long countBlog();
}
