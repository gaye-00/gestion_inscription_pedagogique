package sn.uasz.m1.projet.interfaces;

import java.util.List;

public interface GenericService<T> {
    boolean create(T entity);

    boolean update(T entity);

    boolean delete(Long id);

    T findById(Long id);

    List<T> findAll();
}