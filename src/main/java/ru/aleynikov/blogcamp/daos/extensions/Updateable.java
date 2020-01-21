package ru.aleynikov.blogcamp.daos.extensions;

public interface Updateable {
    
    void update(String query, Object[] qparams);
}
