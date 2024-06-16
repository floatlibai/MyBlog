package com.dev.myblog.service;

import com.dev.myblog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type getTypeById(Long id);

    Type getTypeByName(String name);

    Type updateType(Long id, Type type);

    Page<Type> listType(Pageable pageable);

    void deleteType(Long id);
}
