package services.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import services.interfaces.ICRUD;
import util.JPAConexion;

import java.util.Collections;
import java.util.List;

public class MyDao implements ICRUD {

    @Override
    public <T> List<T> getAll(String nameQuery, Class<T> clazz) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            TypedQuery<T> query = em.createNamedQuery(nameQuery, clazz);
            return query.getResultList();
        } catch (Exception ex) {ex.printStackTrace(); }
        finally { em.close(); }
        return Collections.emptyList();
    }

    @Override
    public <T> void insert(T entity) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }
    @Override
    public <T> void update(T entity) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
}
    @Override
    public <T> void delete(T entity) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }
    @Override
    public <T> T findById(Long id, Class<T> clazz) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            T entity = em.find(clazz, id);
            return entity;
        }catch(Exception ex){ex.printStackTrace();}
        finally { em.close(); }
        return null;
        }

    // Implementación para compatibilidad con firmas existentes que usan Integer
    @Override
    public <T> T findById(Integer id, Class<T> clazz) {
        if (id == null) return null;
        return findById(Long.valueOf(id), clazz);
    }

    // Nuevo: asignar autor y categoria dentro de una sola transacción/EntityManager
    public void asignarAutorYCategoriaALibro(Long libroId, Long autorId, Long categoriaId) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            em.getTransaction().begin();
            Object libro = em.find(Class.forName("entities.Libro"), libroId);
            Object autor = em.find(Class.forName("entities.Autor"), autorId);
            Object categoria = em.find(Class.forName("entities.Categoria"), categoriaId);

            if (libro == null) {
                System.out.println("No se encontró el libro con id: " + libroId);
                em.getTransaction().rollback();
                return;
            }
            if (autor == null) {
                System.out.println("No se encontró el autor con id: " + autorId);
                em.getTransaction().rollback();
                return;
            }
            if (categoria == null) {
                System.out.println("No se encontró la categoría con id: " + categoriaId);
                em.getTransaction().rollback();
                return;
            }

            // Usamos reflection segura: las entidades tienen setters estándar
            // Esto evita dependencias fuertes en el paquete entities desde el DAO.
            Class<?> libroClass = libro.getClass();
            Class<?> autorClass = autor.getClass();
            Class<?> categoriaClass = categoria.getClass();

            libroClass.getMethod("setAutor", autorClass).invoke(libro, autor);
            libroClass.getMethod("setCategoria", categoriaClass).invoke(libro, categoria);

            em.merge(libro);
            em.getTransaction().commit();
            System.out.println("Asignación realizada (en una transacción): libroId=" + libroId + ", autorId=" + autorId + ", categoriaId=" + categoriaId);
        } catch (Exception ex) {
            try { if (em.getTransaction().isActive()) em.getTransaction().rollback(); } catch (Exception e) { }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }
    }