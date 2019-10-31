package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpls.TagDaoImpl;
import ru.aleynikov.blogcamp.models.Post;
import ru.aleynikov.blogcamp.models.Tag;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagDaoImpl tagDao;

    public List<Tag> getNewestTagList(int page,  int tagsOnPageLimit) {
        return tagDao.findNewestTags(FilterDataManager.filterOffset(page, tagsOnPageLimit), tagsOnPageLimit);
    }

    public List<Tag> getTagListBySearch(int page, int tagsOnPageLimit, String filter) {
        return tagDao.findByNameTagsList(FilterDataManager.filterOffset(page, tagsOnPageLimit), tagsOnPageLimit, filter);
    }

    public int getTagsCountBySearch(String filter) {
        return tagDao.countByName(filter);
    }

    public int getAllTagsCount() {
        return tagDao.count();
    }

    public Tag findTagByName(String name) { return tagDao.findTagByName(name); }

    public void saveTag(String name) { tagDao.save(name); }

    public List<Tag> findTagsByPostId(int id) { return tagDao.findTagsByPostId(id); }

    public void updateDescriptionById(String description, int id) {
        if (description.isEmpty())
            description = null;

        tagDao.updateDescriptionById(description, id);
    }

    public void updateTagsCountsOfPostByPostId(Post post) {
        tagDao.updateTagsCountsOfPostByPostId(post);
    }

    public List<Tag> findTagsSortedByNameAsc(int page, int componentsLimit) {
        return tagDao.findTagsSortedByNameAsc(FilterDataManager.filterOffset(page, componentsLimit), componentsLimit);
    }

    public List<Tag> findTopPopularTags(int limit) {
        return tagDao.findPopularTagsWithLimit(limit);
    }
}
