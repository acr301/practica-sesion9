package services.dao;

import entities.Autor;
import entities.Categoria;
import entities.Libro;
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
            Libro libro = em.find(Libro.class, libroId);
            Autor autor = em.find(Autor.class, autorId);
            Categoria categoria = em.find(Categoria.class, categoriaId);

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

            libro.setAutor(autor);
            libro.setCategoria(categoria);

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

    // Nuevo: obtener libro con relaciones inicializadas para evitar lazy issues
    public Libro findLibroWithRelations(Long id) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            Libro libro = em.find(Libro.class, id);
            if (libro != null) {
                if (libro.getAutor() != null) libro.getAutor().getNombre();
                if (libro.getCategoria() != null) libro.getCategoria().getNombre();
            }
            return libro;
        } catch (Exception ex) { ex.printStackTrace(); }
        finally { em.close(); }
        return null;
    }
    }