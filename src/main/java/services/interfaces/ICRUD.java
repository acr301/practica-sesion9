package services.interfaces;
import java.util.List;

public interface ICRUD {
    <T> List<T> getAll(String nameQuery, Class<T> clazz);
    <T>void insert(T entity);
    <T>void update(T entity);
    <T>void delete(T entity);
    <T> T findById(Long id, Class<T> clazz);
    <T> T findById(Integer id, Class<T> clazz);
}
