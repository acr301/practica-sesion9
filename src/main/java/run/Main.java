package run;

import entities.Autor;
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
        a = dao.findById(1, Autor.class);
        a.setNacionalidad("Colombiana");
        dao.update(a);
    }

    public static void eliminarAutor() {
        Autor a = new Autor();
        a = dao.findById(2, Autor.class);
        dao.delete(a);
    }

    public static void main(String[] args) {
        insertarAutor();
        listarAutores();
        editarAutor();
        listarAutores();
        eliminarAutor();
        listarAutores();
    }
}
