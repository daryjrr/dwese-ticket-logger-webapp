package org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Province` representa una entidad que modela una provincia dentro de la base de datos.
 * Contiene cuatro campos: `id`, `code`, `name`, y `region`.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "provinces") // Define el nombre de la tabla asociada a esta entidad.
@Data // Aquí mantenemos Lombok, si te da fallo con los getters, avísame y los generamos también.
@NoArgsConstructor
@AllArgsConstructor
public class Province {

    // Campo que almacena el identificador único de la provincia.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el código de la provincia.
    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 2, message = "{msg.province.code.size}")
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    // Campo que almacena el nombre completo de la provincia.
    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Relación con la entidad `Region`.
    @NotNull(message = "{msg.province.region.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    /**
     * Constructor que excluye el campo `id`.
     * @param code Código de la provincia.
     * @param name Nombre de la provincia.
     * @param region La región a la que pertenece la provincia.
     */
    public Province(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }
}