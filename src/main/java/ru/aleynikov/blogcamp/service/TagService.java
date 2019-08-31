package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.TagDaoImpl;
import ru.aleynikov.blogcamp.model.Tag;

import java.util.List;

@Service
public class TagService {

    private static final int TAGS_ON_PAGE_LIMIT = 36;

    @Autowired
    private TagDaoImpl tagDao;

    public List<Tag> getPopularTagList(int page) {
        int offset = 0;

        if (page == 1)
            page = 0;

        if (page > 0) {
            offset += 36 * page - 1;
        }

        return tagDao.getSortedByPostCountDescTagsList(offset, TAGS_ON_PAGE_LIMIT);
    }
}
