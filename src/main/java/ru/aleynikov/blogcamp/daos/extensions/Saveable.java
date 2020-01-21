package ru.aleynikov.blogcamp.daos.extensions;

public interface Saveable {
    
    void save(String query, Object[] qparams);
}
