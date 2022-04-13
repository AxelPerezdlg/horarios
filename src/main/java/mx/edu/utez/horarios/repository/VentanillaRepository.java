package mx.edu.utez.horarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.edu.utez.horarios.model.Ventanilla;

@Repository
public interface VentanillaRepository extends JpaRepository<Ventanilla, Long> {
    
}
