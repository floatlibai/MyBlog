package com.dev.myblog.web;

import com.dev.myblog.NotFoundException;
import com.dev.myblog.service.BlogService;
import com.dev.myblog.service.TagService;
import com.dev.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexShowController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                            Pageable pageable, Model model) {
        model.addAttribute("page",blogService.listPublishedBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                             Pageable pageable, @RequestParam String query, Model model) {
//        model.addAttribute("page", blogService.listBlog("%"+query+"%", pageable)); // sql like deal
        model.addAttribute("page", blogService.listPublishedBlog(query, pageable)); // sql injection ?
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        if(!blogService.getBlog(id).isPublished()) {
//            return "redirect:/";
            throw new NotFoundException("该博客不存在");
        }
        model.addAttribute("blog", blogService.getAndConvert(id)); // markdown to html
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
