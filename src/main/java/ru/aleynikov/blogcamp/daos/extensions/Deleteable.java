package ru.aleynikov.blogcamp.daos.extensions;

public interface Deleteable {

    void delete(String query, Object[] qparams);
}
