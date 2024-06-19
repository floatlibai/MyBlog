package com.dev.myblog.dao;

import com.dev.myblog.po.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommendable = true")
    List<Blog> findTop(Pageable pageable);


}
