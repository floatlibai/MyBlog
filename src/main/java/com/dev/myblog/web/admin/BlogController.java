package com.dev.myblog.web.admin;

import com.dev.myblog.NotFoundException;
import com.dev.myblog.po.Blog;
import com.dev.myblog.po.User;
import com.dev.myblog.service.BlogService;
import com.dev.myblog.service.TagService;
import com.dev.myblog.service.TypeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.dev.myblog.vo.BlogQuery;

import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC)
                            Pageable pageable, BlogQuery blogQuery, Model model, HttpSession session) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery, session)); // Query in paging mode
        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC)
                        Pageable pageable, BlogQuery blogQuery, Model model, HttpSession session) {

        model.addAttribute("page", blogService.listBlog(pageable, blogQuery, session)); // Query in paging mode
        return "admin/blogs :: blogList"; // Return only blog list fragment
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Blog blog = blogService.getBlog(id);
        if(!Objects.equals(blog.getUser().getId(), user.getId())) {
//            return "redirect:/admin/blogs";
            throw new NotFoundException("该博客不存在");
        }
        blog.init();
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog",blog);
        return "admin/blogs-input";
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }

}
