package com.dev.myblog.service;

import com.dev.myblog.NotFoundException;
import com.dev.myblog.dao.BlogRepository;
import com.dev.myblog.po.Blog;
import com.dev.myblog.po.Type;
import com.dev.myblog.util.Bean_utils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.BlogQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogService_impl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) { // for dynamic query and combined query
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(!"".equals(blogQuery.getTitle()) && blogQuery.getTitle() != null){
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%"+blogQuery.getTitle()+"%"));
                }
                if(blogQuery.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blogQuery.getTypeId()));
                }
                if(blogQuery.isRecommendable()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommendable"), true));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

//    @Override
//    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
//        return blogRepository.findAll(new Specification<Blog>() {
//            @Override
//            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
//                Join join = root.join("tags");
//                return cb.equal(join.get("id"),tagId);
//            }
//        },pageable);
//    }
//
//    @Override
//    public Page<Blog> listBlog(String query, Pageable pageable) {
//        return blogRepository.findByQuery(query,pageable);
//    }
//
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).orElse(null);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, Bean_utils.getNullPropertyNames(blog)); // filter null properties, like creatTime, etc
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
