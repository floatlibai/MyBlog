package com.dev.myblog.service;

import com.dev.myblog.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Tag updateTag(Long id, Tag tag);

    Page<Tag> listTag(Pageable pageable);

    void deleteTag(Long id);

    List<Tag> listTag();

    List<Tag> listTagTop(Integer size);

    List<Tag> listTag(String ids);

}
