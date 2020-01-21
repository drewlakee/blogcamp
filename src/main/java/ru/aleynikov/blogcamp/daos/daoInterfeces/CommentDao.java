package ru.aleynikov.blogcamp.daos.daoInterfeces;

import ru.aleynikov.blogcamp.daos.extensions.Countable;
import ru.aleynikov.blogcamp.daos.extensions.Deleteable;
import ru.aleynikov.blogcamp.daos.extensions.Saveable;
import ru.aleynikov.blogcamp.daos.extensions.StandartQueryable;
import ru.aleynikov.blogcamp.domain.models.Comment;

import java.util.List;

public interface CommentDao extends Saveable, Deleteable, StandartQueryable, Countable {
}
