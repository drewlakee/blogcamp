package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpls.TagDaoImpl;
import ru.aleynikov.blogcamp.daos.TagDao;
import ru.aleynikov.blogcamp.models.Post;
import ru.aleynikov.blogcamp.models.Tag;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    public List<Tag> getNewestTagList(int page, int tagsOnPageLimit) {
        String query = "SELECT DISTINCT tag.tag_id, tag.name, tag.description, tag.created FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false " +
                "ORDER BY created DESC OFFSET ? LIMIT ? ";
        Object[] qparams = new Object[] {
                FilterDataManager.filterOffset(page, tagsOnPageLimit),
                tagsOnPageLimit
        };


        return tagDao.queryForList(query, qparams);
    }

    public List<Tag> getTagListBySearch(int page, int tagsOnPageLimit, String search) {
        String query = "SELECT DISTINCT tag.tag_id, tag.name, tag.description, tag.created FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false " +
                "WHERE LOWER(tag.name) LIKE LOWER(?) " +
                "ORDER BY (created) DESC " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {
                "%"+search+"%",
                FilterDataManager.filterOffset(page, tagsOnPageLimit),
                tagsOnPageLimit
        };

        return tagDao.queryForList(query, qparams);
    }

    public int getTagsCountBySearch(String search) {
        String query = "SELECT COUNT(*) FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false " +
                "WHERE tag.name LIKE ?";
        Object[] qparams = new Object[] {"%"+search+"%"};

        return tagDao.count(query, qparams);
    }

    public int getAllTagsCount() {
        String query = "SELECT COUNT(DISTINCT tag.tag_id) FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false ";

        return tagDao.count(query, null);
    }

    public Tag findTagByName(String name) {
        String query = "SELECT * FROM tag WHERE name = ?";
        Object[] qparams = new Object[] {name};

        return tagDao.queryForObject(query, qparams);
    }

    public void save(String name) {
        String query = "INSERT INTO tag (name, created) VALUES (?, ?)";
        Object[] qparams = new Object[] {
                name,
                new Timestamp(System.currentTimeMillis())
        };

        tagDao.save(query, qparams);
    }

    public List<Tag> getTagsListByPostId(int id) {
        String query = "SELECT * FROM (post_to_tag JOIN tag USING (tag_id)) WHERE post_id = ?";
        Object[] qparams = new Object[] {id};

        return tagDao.queryForList(query, qparams);
    }

    public void updateDescriptionById(String description, int id) {
        String query = "UPDATE tag SET description = ? WHERE tag_id = ?";

        if (description.isEmpty())
            description = null;

        Object[] qparams = new Object[] {description, id};

        tagDao.update(query, qparams);
    }

    public void updateTagsCountOfPostByPostId(Post post) {
        String query = "UPDATE post_to_tag SET tag_id = ? WHERE tag_id = ? AND post_id = ?";
        Object[] qparams;

        for(Tag tag : post.getTags()) {
            qparams = new Object[] {tag.getId(), tag.getId(), post.getId()};
            tagDao.update(query, qparams);
        }
    }

    public List<Tag> getTagsSortedByNameAsc(int page, int componentsLimit) {
        String query = "SELECT tag.tag_id, tag.name, tag.description, tag.created FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false " +
                "ORDER BY name ASC " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {
                FilterDataManager.filterOffset(page, componentsLimit),
                componentsLimit
        };

        return tagDao.queryForList(query, qparams);
    }

    public List<Tag> getTopPopularTags(int limit) {
        String query = "SELECT *, (SELECT COUNT(*) FROM post_to_tag WHERE tag.tag_id = post_to_tag.tag_id) as count FROM tag " +
                "WHERE (SELECT COUNT(*) FROM post_to_tag " +
                "    JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "    JOIN usr on post.\"user\" = usr.user_id AND usr.banned = false " +
                "WHERE tag.tag_id = post_to_tag.tag_id ) > 0 " +
                "ORDER BY count DESC OFFSET 0 LIMIT ? ";
        Object[] qparams = new Object[] {limit};

        return tagDao.queryForList(query, qparams);
    }
}
