package com.dev.myblog.dao;

import com.dev.myblog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Type findByName(String name);

//    @Query("select t from Type t ORDER BY SIZE(t.blogs) DESC")
    @Query("select t from Type t join t.blogs b where b.published = true group by t.id order by count(b) desc")
    List<Type> findTop(Pageable pageable);
}
