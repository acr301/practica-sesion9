package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "autores")
@Getter @Setter
@NamedQueries({
        @NamedQuery(name = "autores.All",
        query = "select a from Autor a"
        )
})
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_autor", length = 60, nullable = false)
    private String nombre;

    @Column(name = "nacionalidad", length = 60, nullable = false)
    private String nacionalidad;

    private LocalDate fechaNac;
}
