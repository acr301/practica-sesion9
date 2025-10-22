package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConexion {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa_db");
    public static EntityManager getEntityManager() { return emf.createEntityManager();}
    public static void close() { emf.close(); }
}
