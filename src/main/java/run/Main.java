package run;

import entities.Autor;
import services.dao.MyDao;
import services.interfaces.ICRUD;

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
}
