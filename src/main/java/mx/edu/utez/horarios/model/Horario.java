package mx.edu.utez.horarios.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;




@Entity
@Table(name = "horarios")
public class Horario {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idhorarios;

    
    
    @Column(name="dia",nullable = false)  
	private Date dia;

    
    @Column(name = "hora_inicio", nullable = false)
    private Date hora_inicio;



    @Column(name = "hora_fin",nullable = false)
	private Date hora_fin;

    
    @Column(name = "repeticiones",nullable = false)
	private Integer repeticiones;

    @ManyToOne
	@JoinColumn(name = "idusuarios", nullable = false)
	private Usuario usuario;

    
    @ManyToOne
	@JoinColumn(name = "idventanilla", nullable = false)
	private Ventanilla ventanilla;


    
    public Horario() {
    }
    

    public Long getIdhorarios() {
        return idhorarios;
    }

    public void setIdhorarios(Long idhorarios) {
        this.idhorarios = idhorarios;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    
  
    public Date getHora_inicio() {
		return hora_inicio;
	}


	public void setHora_inicio(Date hora_inicio) {
		this.hora_inicio = hora_inicio;
	}


	public Date getHora_fin() {
		return hora_fin;
	}


	public void setHora_fin(Date hora_fin) {
		this.hora_fin = hora_fin;
	}


	public Integer getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ventanilla getVentanilla() {
        return ventanilla;
    }

    public void setVentanilla(Ventanilla ventanilla) {
        this.ventanilla = ventanilla;
    }


    




}
