package ru.aleynikov.blogcamp.daos.extensions;

public interface Countable {

    int count(String query, Object[] qparams);
}
