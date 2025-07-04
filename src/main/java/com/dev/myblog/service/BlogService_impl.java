package com.dev.myblog.service;

import com.dev.myblog.NotFoundException;
import com.dev.myblog.dao.BlogRepository;
import com.dev.myblog.po.Blog;
import com.dev.myblog.po.Type;
import com.dev.myblog.po.User;
import com.dev.myblog.util.Bean_utils;
import com.dev.myblog.util.Markdown_utils;
import jakarta.persistence.criteria.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dev.myblog.vo.BlogQuery;

import java.util.*;

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
    public Page<Blog> listPublishedBlog(Pageable pageable, BlogQuery blogQuery) { // for dynamic query and combined query
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
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("published"), true));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listPublishedBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                Predicate tagPredicate = cb.equal(join.get("id"), tagId);
                Predicate publishedPredicate = cb.isTrue(root.get("published"));
                return cb.and(tagPredicate, publishedPredicate);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listPublishedBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

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

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(Markdown_utils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new TreeMap<>(Comparator.reverseOrder());
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        for (Map.Entry<String, List<Blog>> entry : map.entrySet()) {
            List<Blog> list = entry.getValue();
            list.sort(Comparator.comparing(Blog::getUpdateTime).reversed());
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery, HttpSession session) {
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
                User user = (User) session.getAttribute("user");
                predicates.add(criteriaBuilder.equal(root.<User>get("user").get("id"), user.getId()));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listPublishedBlog(Pageable pageable) {
        return blogRepository.findByPublishedTrue(pageable);
    }
}
