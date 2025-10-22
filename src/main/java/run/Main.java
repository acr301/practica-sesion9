package run;

import entities.Autor;
import entities.Categoria;
import entities.Libro;
import services.dao.MyDao;
import services.interfaces.ICRUD;

import java.util.List;

public class Main {
    public static final ICRUD dao = new MyDao();

    public static void insertarAutor() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel Garcia Marquez");
        autor.setNacionalidad("Mexicana");
        dao.insert(autor);

        Autor r = new Autor();
        r.setNombre("Ruben Dario");
        r.setNacionalidad("Nicaraguense");
        dao.insert(r);
    }

    public static void listarAutores() {
        System.out.println("Registro Almacenados");
        List<Autor> autores = dao.getAll("autores.All", Autor.class);
        autores.forEach(autor -> System.out.println(autor.getNombre()));
    }

    public static void editarAutor() {
        Autor a = new Autor();
        a = dao.findById(1L, Autor.class);
        if (a != null) {
            a.setNacionalidad("Colombiana");
            dao.update(a);
        } else {
            System.out.println("Autor con id 1 no encontrado para editar.");
        }
    }

    public static void eliminarAutor() {
        Autor a = new Autor();
        a = dao.findById(2L, Autor.class);
        if (a != null) {
            dao.delete(a);
        } else {
            System.out.println("Autor con id 2 no encontrado para eliminar.");
        }
    }

    /*
        CRUD de categoria
     */
    public static void insertarCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Novela");
        // Guardar la categoría en la base de datos
        dao.insert(categoria);
    }

    public static void listarCategorias() {
        System.out.println("Categorias Almacenados");
        List<Categoria> categorias = dao.getAll("categorias.All", Categoria.class);
        categorias.forEach(categoria -> System.out.println(categoria.getNombre()));
    }

    public static void editarCategoria() {
        Categoria c = new Categoria();
        c = dao.findById(1L, Categoria.class);
        if (c != null) {
            c.setNombre("Realismo Mágico");
            dao.update(c);
        } else {
            System.out.println("Categoría con id 1 no encontrada para editar.");
        }
    }
    public static void eliminarCategoria() {
        Categoria c = new Categoria();
        c = dao.findById(1L, Categoria.class);
        if (c != null) {
            dao.delete(c);
        } else {
            System.out.println("Categoría con id 1 no encontrada para eliminar.");
        }

    }
     /*
        CRUD de libro
     */

    public static void insertarLibro() {
        Libro libro = new Libro();
        libro.setTitulo("Cien Años de Soledad");
        libro.setAnioPub(1967);
        // Intentar asignar un autor y categoría por defecto si existen (id = 1)
        Autor autor = dao.findById(1L, Autor.class);
        if (autor != null) {
            libro.setAutor(autor);
        }
        Categoria categoria = dao.findById(1L, Categoria.class);
        if (categoria != null) {
            libro.setCategoria(categoria);
        }
        dao.insert(libro);
    }

    public static void listarLibros() {
        System.out.println("Libros Almacenados");
        List<Libro> libros = dao.getAll("libros", Libro.class);
        libros.forEach(libro -> System.out.println(libro.getTitulo()));
    }

    public static void editarLibro() {
        Libro l = new Libro();
        l = dao.findById(1L, Libro.class);
        if (l != null) {
            l.setAnioPub(1970);
            dao.update(l);
        } else {
            System.out.println("Libro con id 1 no encontrado para editar.");
        }
    }

    public static void eliminarLibro() {
        Libro l = new Libro();
        l = dao.findById(1L, Libro.class);
        if (l != null) {
            dao.delete(l);
        } else {
            System.out.println("Libro con id 1 no encontrado para eliminar.");
        }
    }

    /**
     * Asigna un autor y una categoría a un libro existente.
     * Realiza comprobaciones nulas e informa por consola.
     * Usa el método atómico de MyDao cuando está disponible para evitar problemas con entidades detached.
     * @param libroId id del libro a asignar
     * @param autorId id del autor a asignar
     * @param categoriaId id de la categoría a asignar
     */
    public static void asignarLibroYCategoria(Long libroId, Long autorId, Long categoriaId) {
        if (dao instanceof MyDao) {
            // Usar el método atómico que abre una sola EntityManager/Transacción
            ((MyDao) dao).asignarAutorYCategoriaALibro(libroId, autorId, categoriaId);
            return;
        }

        // Fallback genérico usando las operaciones del ICRUD: findById + update
        Libro libro = dao.findById(libroId, Libro.class);
        if (libro == null) {
            System.out.println("No se encontró el libro con id: " + libroId);
            return;
        }

        Autor autor = dao.findById(autorId, Autor.class);
        if (autor == null) {
            System.out.println("No se encontró el autor con id: " + autorId);
            return;
        }

        Categoria categoria = dao.findById(categoriaId, Categoria.class);
        if (categoria == null) {
            System.out.println("No se encontró la categoría con id: " + categoriaId);
            return;
        }

        libro.setAutor(autor);
        libro.setCategoria(categoria);
        dao.update(libro);

        System.out.println("Asignación realizada: libro='" + libro.getTitulo() + "', autor='" + autor.getNombre() + "', categoria='" + categoria.getNombre() + "'");
    }

    /**
     * Versión sin parámetros que usa ids por defecto (1)
     */
    public static void asignarLibroYCategoria() {
        asignarLibroYCategoria(1L, 1L, 1L);
    }


    public static void main(String[] args) {
        insertarAutor();
        listarAutores();
        editarAutor();
        listarAutores();
        eliminarAutor();
        listarAutores();

        /*
            prueba crud categoria
         */

        insertarCategoria();
        listarCategorias();
        editarCategoria();
        listarCategorias();
        eliminarCategoria();
        listarCategorias();

        /*
            prueba crud libro
         */
        insertarLibro();
        listarLibros();
        // Asignar explícitamente (si necesitamos otro id usar la sobrecarga con parámetros)
        asignarLibroYCategoria();
        listarLibros();
    }
}
