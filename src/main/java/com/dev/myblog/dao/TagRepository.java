package com.dev.myblog.dao;

import com.dev.myblog.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

//    @Query("select t from Tag t ORDER BY SIZE(t.blogs) DESC")
    @Query("select t from Tag t join t.blogs b where b.published = true group by t.id order by count(b) desc")
    List<Tag> findTop(Pageable pageable);
}
