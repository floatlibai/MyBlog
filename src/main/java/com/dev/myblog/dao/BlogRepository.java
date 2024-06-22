package com.dev.myblog.dao;

import com.dev.myblog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommendable = true and b.published = true")
    List<Blog> findTop(Pageable pageable);

//    @Query("select b from Blog b where b.title like ?1 or b.description like ?1")
//    @Query("select b from Blog b where b.title like %:query% or b.description like %:query%")
//    @Query("select b from Blog b where (b.title like %:query% or b.description like %:query%) and b.published = true")
    @Query("select b from Blog b where (lower(b.title) like lower(concat('%', :query, '%')) or lower(b.description) like lower(concat('%', :query, '%'))) and b.published = true")
    Page<Blog> findByQuery(@Param("query") String query, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b where b.published = true group by function('date_format',b.updateTime,'%Y') order by function('date_format',b.updateTime,'%Y') desc ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1 and b.published = true")
    List<Blog> findByYear(String year);

    Page<Blog> findByPublishedTrue(Pageable pageable);
}
