package mx.edu.utez.horarios.service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import mx.edu.utez.horarios.model.Horario;
import mx.edu.utez.horarios.repository.HorarioRepository;

@Service
public class HorarioService {
    
    
    @Autowired
	private HorarioRepository horarioRepository;

	public List<Horario> listar() {
		return horarioRepository.findAll(Sort.by("dia"));
	}
	
    
	public boolean guardar(Horario horario) {
		try {
			horarioRepository.save(horario);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		
	}


    public Horario mostrar(long idhorarios) {
		Optional<Horario> optional = horarioRepository.findById(idhorarios);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
    
	public boolean eliminar(long idhorarios) {
		boolean existe = horarioRepository.existsById(idhorarios);
		if (existe) {
			horarioRepository.deleteById(idhorarios);
			return !horarioRepository.existsById(idhorarios);
		}
		
		return false;
		
	}


    public Page<Horario> listarPaginacion(Pageable page) {
		return horarioRepository.findAll(page);
	}

	public List<Horario> listarPorHora(String horaInicioRegistro, String horaFinalRegistro) {
		return horarioRepository.findByhora_inicioAndhora_fin(horaInicioRegistro,horaFinalRegistro);
   	}

	public List<Horario> listarPorDiaAndVentanilla(Date dia, Long idVentanilla) {
		return horarioRepository.findByDiaAndVentanilla(dia, idVentanilla);
   	}

	public List<Horario> listarPorDiaAndVentanillaAndHorario(Date dia, Long idVentanilla, Long idHorarios) {
		return horarioRepository.findByDiaAndVentanillaAndHorario(dia, idVentanilla, idHorarios);
   	}


}
