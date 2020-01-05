package ru.aleynikov.blogcamp.daos.daoInterfeces;

import ru.aleynikov.blogcamp.daos.extensions.*;
import ru.aleynikov.blogcamp.domain.models.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostDao extends Saveable, Updateable, StandartQueryable, Countable, Deleteable {

    int findPostIdByUserIdAndTitle(int userId, String title);

    void setTagsToPost(Set<String> tags, HashMap<String, Object> post);
    void removeTagsFromPost(Set<String> tags, HashMap<String, Object> post);
}
