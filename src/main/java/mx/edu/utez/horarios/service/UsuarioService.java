package mx.edu.utez.horarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mx.edu.utez.horarios.model.Usuario;
import mx.edu.utez.horarios.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
	private UsuarioRepository usuarioRepository;

	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}
	
    
	public boolean guardar(Usuario usuario) {
		try {
			usuarioRepository.save(usuario);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		
	}


    
	public boolean eliminar(long id) {
		boolean existe = usuarioRepository.existsById(id);
		if (existe) {
			usuarioRepository.deleteById(id);
			return !usuarioRepository.existsById(id);
		}
		
		return false;
		
	}


    public Page<Usuario> listarPaginacion(Pageable page) {
		return usuarioRepository.findAll(page);
	}


}
