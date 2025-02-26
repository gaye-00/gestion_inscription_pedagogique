package sn.uasz.m1.projet.interfaces;



import java.util.List;

public interface GenericService<T> {
    void add(T entity);
    void update(T entity);
    void delete(Long id);
    T getById(Long id);
    List<T> getAll();
}
