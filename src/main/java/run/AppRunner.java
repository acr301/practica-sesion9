package run;

import entities.Autor;
import entities.Categoria;
import entities.Libro;
import services.dao.MyDao;
import util.JPAConexion;

public class AppRunner {
    public static void main(String[] args) {
        MyDao dao = new MyDao();
        try {
            // 1) Insertar autor
            Autor autor = new Autor();
            autor.setNombre("Isabel Allende");
            autor.setNacionalidad("Chilena");
            dao.insert(autor);
            System.out.println("Autor creado con id: " + autor.getId());

            // 2) Insertar categoría
            Categoria categoria = new Categoria();
            categoria.setNombre("Ficción");
            dao.insert(categoria);
            System.out.println("Categoría creada con id: " + categoria.getId());

            // 3) Insertar libro sin relaciones
            Libro libro = new Libro();
            libro.setTitulo("La Casa de los Espíritus");
            libro.setAnioPub(1982);
            dao.insert(libro);
            System.out.println("Libro creado con id: " + libro.getId());

            // 4) Asignar autor y categoría al libro usando el método atómico del DAO
            dao.asignarAutorYCategoriaALibro(libro.getId(), autor.getId(), categoria.getId());

            // 5) Recuperar el libro con relaciones inicializadas y mostrar detalles
            Libro loaded = dao.findLibroWithRelations(libro.getId());
            if (loaded != null) {
                System.out.println("--- Libro cargado ---");
                System.out.println("Id: " + loaded.getId());
                System.out.println("Titulo: " + loaded.getTitulo());
                System.out.println("Año: " + loaded.getAnioPub());
                if (loaded.getAutor() != null) System.out.println("Autor: " + loaded.getAutor().getNombre());
                if (loaded.getCategoria() != null) System.out.println("Categoria: " + loaded.getCategoria().getNombre());
            } else {
                System.out.println("No se pudo cargar el libro con id: " + libro.getId());
            }

            // 6) Mostrar todos los libros (títulos)
            System.out.println("Lista de títulos de libros:");
            dao.getAll("libros", Libro.class).forEach(l -> System.out.println(" - " + l.getTitulo()));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Cerrar la fábrica de EntityManager para evitar hilos colgando
            try { JPAConexion.close(); } catch (Exception e) { /* ignore */ }
        }
    }
}
