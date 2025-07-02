package com.dev.myblog.service;

import com.dev.myblog.NotFoundException;
import com.dev.myblog.dao.BlogRepository;
import com.dev.myblog.dao.TypeRepository;
import com.dev.myblog.po.Blog;
import com.dev.myblog.po.Type;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TypeService_impl implements TypeService{

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).orElse(null);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
//        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
//        Pageable pageable = PageRequest.of(0,size,sort);
        Pageable pageable = PageRequest.of(0,size);
        return typeRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).orElse(null);
        if (t != null) {
            throw new NotFoundException("不存在该分类");
        }
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        if(id == 0) { // set default id 0
            throw new IllegalArgumentException("Default category cannot be deleted.");
        }
        Type defaultType = typeRepository.findById(0L).orElseThrow(() -> new EntityNotFoundException("Default category not found"));
        List<Blog> blogs = blogRepository.findByTypeId(id);
        for (Blog blog : blogs) {
            blog.setType(defaultType);
            blogRepository.save(blog);
        }
        typeRepository.deleteById(id);
    }
}
