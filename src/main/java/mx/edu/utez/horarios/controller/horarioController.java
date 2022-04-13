package mx.edu.utez.horarios.controller;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.edu.utez.horarios.model.Horario;
import mx.edu.utez.horarios.service.HorarioService;
import mx.edu.utez.horarios.service.UsuarioService;
import mx.edu.utez.horarios.service.VentanillaService;

@Controller
@RequestMapping(value = "/horarios")
public class horarioController {
    
    @Autowired
    private HorarioService horarioService;

    @Autowired
    private VentanillaService ventanillaService;

	@Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/listar" )
	public String listarHorarios( Model modelo, Pageable pageable) {
        Page<Horario> listaHorarios = horarioService.listarPaginacion(PageRequest.of(pageable.getPageNumber(), 5, Sort.by("dia").ascending()));
		modelo.addAttribute("listaHorarios", listaHorarios);
		return "listaHorarios";
	}

    @GetMapping("/crear")
	public String crearHorario(Horario horario, Model modelo) {
		modelo.addAttribute("listaVentanilla", ventanillaService.listar());
		modelo.addAttribute("listaUsuarios", usuarioService.listar());
		LocalDate now = LocalDate.now();
		modelo.addAttribute("now", now);
		return "formHorarios";
	}
  

    @GetMapping("/editar/{idhorarios}")
	public String editar(@PathVariable long idhorarios, Model modelo, RedirectAttributes redirectAttributes) {
		Horario horario = horarioService.mostrar(idhorarios);
		if (horario != null) {
			modelo.addAttribute("horario", horario);
			SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatterHour = new SimpleDateFormat("HH:mm");
	
			modelo.addAttribute("fechaDiaUpdate", formatterDate.format(horario.getDia()));
			modelo.addAttribute("horaInicioUpdate", formatterHour.format(horario.getHora_inicio()));
			modelo.addAttribute("horaFinUpdate", formatterHour.format(horario.getHora_fin()));
			
			modelo.addAttribute("listaVentanilla", ventanillaService.listar());
			modelo.addAttribute("listaUsuarios", usuarioService.listar());
			return "editHorarios";
		}
		redirectAttributes.addFlashAttribute("msg_error", "Registro No Encontrado");
		return "redirect:/horarios/listar";
	}


    @PostMapping("/guardar")
	public String guardarHorario(
	@RequestParam("fechaDia") String fechaDia, 
	@RequestParam("horaInicio") String horaInicio,
	@RequestParam("horaFinal") String horaFinal ,Horario horario, Model modelo, RedirectAttributes redirectAttributes,  BindingResult result) throws ParseException {
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String horaInicioRegistro = fechaDia + " " + horaInicio + ":00";
		String horaFinalRegistro = fechaDia + " " + horaFinal + ":00";
		Date fechaDiaRegistro =  formatter.parse(fechaDia + " 00:00");
		Date inicioH = formatter.parse(horaInicioRegistro);
		Date finH = formatter.parse(horaFinalRegistro);

		System.out.println("hora inicio: " + horaInicio);
		
		String errorMessage = "";
		boolean bandera = true;
		String successMessage ="";		

		if (horario.getIdhorarios() == null) { // Create
			
			List<Horario> listaDia = horarioService.listarPorDiaAndVentanilla(fechaDiaRegistro, horario.getVentanilla().getIdventanilla());

			for (int i = 0; i < listaDia.size(); i++) {
				if(inicioH.getTime() >= listaDia.get(i).getHora_inicio().getTime() || finH.getTime()<= listaDia.get(i).getHora_fin().getTime()){
					errorMessage = "La ventanilla " + listaDia.get(i).getVentanilla().getNumero()  + " no puede registrar entre las horas de otro horario";
					bandera = false;
				}
			}
			if(bandera){
				horario.setDia(fechaDiaRegistro);
				horario.setHora_inicio(inicioH);
				horario.setHora_fin(finH);
				successMessage = "Registro Creado Exitosamente";
			}

			
		
		} else { // Update
			
			List<Horario> listaUpdate = horarioService.listarPorDiaAndVentanillaAndHorario(fechaDiaRegistro, horario.getVentanilla().getIdventanilla(), horario.getIdhorarios());
			for (int i = 0; i < listaUpdate.size(); i++) {
				if(inicioH.getTime() >= listaUpdate.get(i).getHora_inicio().getTime() || finH.getTime()<= listaUpdate.get(i).getHora_fin().getTime()){
					errorMessage = "No se puede poner fechas encimadas";
					bandera = false;
				}
			}
			if(bandera){
			
				horario.setDia(fechaDiaRegistro);
				horario.setHora_inicio(inicioH);
				horario.setHora_fin(finH);
				horario.setRepeticiones(horario.getRepeticiones());
				horario.setUsuario(horario.getUsuario());
				successMessage = "Registro Modificado Exitosamente";
			}
		}
		
		boolean respuesta = false;

		if(bandera){
			respuesta = horarioService.guardar(horario);
			if(!respuesta){
				errorMessage =  "Registro Fallido por favor intente de nuevo";
			}
		}
		
		
	
		if (respuesta) {
			redirectAttributes.addFlashAttribute("msg_success", successMessage);
			return "redirect:/horarios/listar";
		}else {
			redirectAttributes.addFlashAttribute("msg_error", errorMessage);
			if (horario.getIdhorarios() == null) {
				return "redirect:/horarios/crear";
			}else{
				return "redirect:/horarios/editar/"+ horario.getIdhorarios();
			}
		
		}
	}

    @GetMapping("/eliminar/{idhorarios}")
	public String eliminarHorario(@PathVariable long idhorarios, RedirectAttributes redirectAttributes) {
		boolean respuesta = horarioService.eliminar(idhorarios);
		if (respuesta) {
			redirectAttributes.addFlashAttribute("msg_success", "Registro Eliminado");
			
		}else {
			redirectAttributes.addFlashAttribute("msg_error", "Eliminacion Fallida");
			
		}
		return "redirect:/horarios/listar";
	}

	
	

}
