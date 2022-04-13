package mx.edu.utez.horarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import mx.edu.utez.horarios.model.Ventanilla;
import mx.edu.utez.horarios.repository.VentanillaRepository;

@Service
public class VentanillaService {
    
    @Autowired
	private VentanillaRepository ventanillaRepository;

	public List<Ventanilla> listar() {
		return ventanillaRepository.findAll();
	}
	
    
	public boolean guardar(Ventanilla ventanilla) {
		try {
			ventanillaRepository.save(ventanilla);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		
	}


    
	public boolean eliminar(long id) {
		boolean existe = ventanillaRepository.existsById(id);
		if (existe) {
			ventanillaRepository.deleteById(id);
			return !ventanillaRepository.existsById(id);
		}
		
		return false;
		
	}


    public Page<Ventanilla> listarPaginacion(Pageable page) {
		return ventanillaRepository.findAll(page);
	}

}
