package com.zhukowez.repository;

import java.util.List;

public interface CRUDRepository<K, T> {


    T create(T object);

    T findOne(K id); //

    List<T> findAll();

    T update(T object);

    void delete(K id);
}
