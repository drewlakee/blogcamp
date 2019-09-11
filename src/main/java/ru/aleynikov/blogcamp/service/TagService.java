package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.TagDaoImpl;
import ru.aleynikov.blogcamp.model.Tag;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagDaoImpl tagDao;

    private int filterOffset(int page) {
        int offset = 0;

        if (page == 1 || page < 0)
            page = 0;

        if (page > 0) {
            offset += 36 * (page - 1);
        }

        return offset;
    }

    public List<Tag> getPopularTagList(int page,  int tagsOnPageLimit) {

        return tagDao.getSortedByPostCountTagsList(filterOffset(page), tagsOnPageLimit);
    }

    public List<Tag> getNewestTagList(int page,  int tagsOnPageLimit) {

        return tagDao.getSortedByCreatedDateNewestTagsList(filterOffset(page), tagsOnPageLimit);
    }

    public List<Tag> getSearchTagList(int page, int tagOnPageLimit, String filter) {

        return tagDao.getSearchByNameTagsList(filterOffset(page), tagOnPageLimit, filter);
    }

    public int getSearchTagsCount(String filter) {
        return tagDao.getSearchByNameCount(filter);
    }

    public int getAllTagsCount() {
        return tagDao.getTagsCount();
    }
}
