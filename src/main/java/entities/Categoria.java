package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter @Setter
@NamedQueries(
        @NamedQuery(name = "categorias.All",
                query = "select c from Categoria c"
        )
)
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_categoria", length = 100, nullable = false)
    private String nombre;
}
