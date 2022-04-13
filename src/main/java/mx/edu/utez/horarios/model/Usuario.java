package mx.edu.utez.horarios.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idusuarios;

    @Column(name = "nombre",nullable = false, length = 45)
	private String nombre;

    public Usuario() {
    }

    public Long getIdusuarios() {
        return idusuarios;
    }

    public void setIdusuarios(Long idusuarios) {
        this.idusuarios = idusuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    


}
