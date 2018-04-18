package repository;

import validator.ValidationException;

public interface IRepository<ID,T> {
    Integer size();
    void save(T entity) throws ValidationException;
    void delete(ID id);
    void update(ID id, T entity) throws ValidationException;
    T findOne(ID id);
    Iterable<T> findAll();
}