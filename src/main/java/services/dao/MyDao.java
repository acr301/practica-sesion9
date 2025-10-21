package services.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import services.interfaces.ICRUD;
import util.JPAConexion;

import java.util.List;

public class MyDao implements ICRUD {

    @Override
    public <T> List<T> getAll(String nameQuery, Class<T> clazz) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            TypedQuery<T> query = em.createNamedQuery(nameQuery, clazz);
        } catch (Exception ex) {ex.printStackTrace(); }
        finally { em.close(); }
        return null;
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
    public <T> T findById(Integer id, Class<T> clazz) {
        EntityManager em = JPAConexion.getEntityManager();
        try {
            T entity = em.find(clazz, id);
            return entity;
        }catch(Exception ex){ex.printStackTrace();}
        finally { em.close(); }
        return null;
        }
    }