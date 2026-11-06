package org.alixar.daw2.dario.dwese_ticket_logger_webapp.dwese_ticket_logger_webapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
// Quitamos @Data temporalmente si te da fallos y usamos los getters/setters manuales

/**
 * La clase `Region` representa una entidad que modela una región dentro de la base de datos.
 * Contiene tres campos: `id`, `code` y `name`, donde `id` es el identificador único de la región,
 * `code` es un código asociado a la región, y `name` es el nombre de la región.
 */
@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "regions") // Especifica el nombre de la tabla asociada a esta entidad.
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    // Campo que almacena el identificador único de la región.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el código de la región, normalmente una cadena corta.
    @NotEmpty(message = "{msg.region.code.notEmpty}")
    @Size(max = 2, message = "{msg.region.code.size}")
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    // Campo que almacena el nombre completo de la región.
    @NotEmpty(message = "{msg.region.name.notEmpty}")
    @Size(max = 100, message = "{msg.region.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Constructor sin ID
     * @param code Código de la región.
     * @param name Nombre de la región.
     */
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // --- GETTERS Y SETTERS EXPLÍCITOS (Para solucionar el error de tu imagen) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}