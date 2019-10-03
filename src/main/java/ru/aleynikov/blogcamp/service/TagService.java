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

    public List<Tag> getPopularTagList(int page,  int tagsOnPageLimit) {
        return tagDao.getSortedByPostCountTagsList(FilterDataManager.filterOffset(page, tagsOnPageLimit), tagsOnPageLimit);
    }

    public List<Tag> getNewestTagList(int page,  int tagsOnPageLimit) {
        return tagDao.getSortedByCreatedDateNewestTagsList(FilterDataManager.filterOffset(page, tagsOnPageLimit), tagsOnPageLimit);
    }

    public List<Tag> getFilterTagList(int page, int tagsOnPageLimit, String filter) {
        return tagDao.getSearchByNameTagsList(FilterDataManager.filterOffset(page, tagsOnPageLimit), tagsOnPageLimit, filter);
    }

    public int getFilterTagsCount(String filter) {
        return tagDao.getFilterByNameCount(filter);
    }

    public int getAllTagsCount() {
        return tagDao.getTagsCount();
    }

    public Tag findTagByName(String name) { return tagDao.findTagByName(name); }

    public void saveTag(String name) { tagDao.saveTag(name); }
}
