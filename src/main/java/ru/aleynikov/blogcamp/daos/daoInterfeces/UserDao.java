package ru.aleynikov.blogcamp.daos.daoInterfeces;

import ru.aleynikov.blogcamp.daos.extensions.Countable;
import ru.aleynikov.blogcamp.daos.extensions.Saveable;
import ru.aleynikov.blogcamp.daos.extensions.StandartQueryable;
import ru.aleynikov.blogcamp.daos.extensions.Updateable;
import ru.aleynikov.blogcamp.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Saveable, Updateable, StandartQueryable, Countable {

    void ban(String query, Object[] qprams);
    void unban(String query, Object[] qparams);
}
