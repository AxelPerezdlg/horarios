package mx.edu.utez.horarios.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name = "ventanillas")
public class Ventanilla {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idventanilla;

    @Column(name = "numero",nullable = false, length = 2)
	private Integer numero;


    


    public Ventanilla() {
    }


    public Long getIdventanilla() {
        return idventanilla;
    }


    public void setIdventanilla(Long idventanilla) {
        this.idventanilla = idventanilla;
    }


    public Integer getNumero() {
        return numero;
    }


    public void setNumero(Integer numero) {
        this.numero = numero;
    }


  
    
    

}
