package mx.edu.utez.horarios.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
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

	@GetMapping(value = "/listar")
	public String listarHorarios(Model modelo, Pageable pageable) {
		Page<Horario> listaHorarios = horarioService
				.listarPaginacion(PageRequest.of(pageable.getPageNumber(), 5, Sort.by("dia").ascending()));
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
			@RequestParam("horaFinal") String horaFinal, Horario horario, Model modelo, RedirectAttributes redirectAttributes, BindingResult result) throws ParseException {

		boolean isRegistro = true;
		if(horario.getIdhorarios() != null){
			isRegistro = false;
		}

		int repeticiones = 1;
		if(horario.getRepeticiones() != null){
			repeticiones = horario.getRepeticiones();
		}
		boolean respuesta = false;
		String successMessage = "";
		String errorMessage = "";
		String fechaTempo = fechaDia;
		int registrosExitosos = 0;
			

		for (int x = 0; x < repeticiones; x++) {
			

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			String horaInicioRegistro = fechaTempo + " " + horaInicio + ":00";
			String horaFinalRegistro = fechaTempo + " " + horaFinal + ":00";
			Date fechaDiaRegistro = formatter.parse(fechaTempo + " 00:00");
			Date inicioH = formatter.parse(horaInicioRegistro);
			Date finH = formatter.parse(horaFinalRegistro);

			System.out.println("fechaTempo: "+ fechaTempo);

			
			System.out.println("id: "+ horario.getIdhorarios());
			boolean bandera = true;

			if (isRegistro) { // Create
				
				System.out.println("se hizo un create");
				List<Horario> listaDia = horarioService.listarPorDiaAndVentanilla(fechaDiaRegistro, horario.getVentanilla().getIdventanilla());

				for (int i = 0; i < listaDia.size(); i++) {
					if (inicioH.getTime() >= listaDia.get(i).getHora_inicio().getTime() || finH.getTime() <= listaDia.get(i).getHora_fin().getTime()) {
						errorMessage = "La ventanilla " + listaDia.get(i).getVentanilla().getNumero() + " no puede registrar entre las horas de otro horario";
						bandera = false;
					}
				}
				if (bandera) {
					horario.setDia(fechaDiaRegistro);
					horario.setHora_inicio(inicioH);
					horario.setHora_fin(finH);
					successMessage = "Registro Creado Exitosamente";
				}

			} else { // Update
				System.out.println("se hizo un update");
				List<Horario> listaUpdate = horarioService.listarPorDiaAndVentanillaAndHorario(fechaDiaRegistro,
						horario.getVentanilla().getIdventanilla(), horario.getIdhorarios());
				for (int i = 0; i < listaUpdate.size(); i++) {
					if (inicioH.getTime() >= listaUpdate.get(i).getHora_inicio().getTime()
							|| finH.getTime() <= listaUpdate.get(i).getHora_fin().getTime()) {
						errorMessage = "No se puede poner fechas encimadas";
						bandera = false;
					}
				}
				if (bandera) {

					horario.setDia(fechaDiaRegistro);
					horario.setHora_inicio(inicioH);
					horario.setHora_fin(finH);
					horario.setRepeticiones(horario.getRepeticiones());
					horario.setUsuario(horario.getUsuario());
					successMessage = "Registro Modificado Exitosamente";
					
				}
				
			}

			if (bandera) {
				Horario horarioTempo = new Horario();
				horarioTempo.setDia(horario.getDia());
				horarioTempo.setHora_inicio(horario.getHora_inicio());
				horarioTempo.setHora_fin(horario.getHora_fin());
				horarioTempo.setRepeticiones(1);
				horarioTempo.setUsuario(horario.getUsuario());
				horarioTempo.setVentanilla(horario.getVentanilla());

				if(!isRegistro){
					horarioTempo.setIdhorarios(horario.getIdhorarios());
				}
				respuesta = horarioService.guardar(horarioTempo);
				if (!respuesta) {
					errorMessage = "Registro Fallido por favor intente de nuevo";
				}else{
					registrosExitosos = registrosExitosos + 1;
				}

				if(!isRegistro){
					break;
				}
			}

			SimpleDateFormat formatterTempo = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaCambio = formatter.parse(fechaTempo + " 00:00");
		
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaCambio);            
			calendar.add(Calendar.DAY_OF_YEAR, 7);
			Date dateOneWeek = calendar.getTime();
			fechaTempo = formatterTempo.format(dateOneWeek);

		}

		

		System.out.println("registros exitosos: " + registrosExitosos);

		if (registrosExitosos > 0) {
			if (isRegistro){
				redirectAttributes.addFlashAttribute("msg_success", successMessage + ", " + registrosExitosos +" Registros Exitosos");
			}
			
			return "redirect:/horarios/listar";
		} else {
			redirectAttributes.addFlashAttribute("msg_error", errorMessage);
			if (isRegistro) {
				return "redirect:/horarios/crear";
			} else {
				return "redirect:/horarios/editar/" + horario.getIdhorarios();
			}

		}
	}

	@GetMapping("/eliminar/{idhorarios}")
	public String eliminarHorario(@PathVariable long idhorarios, RedirectAttributes redirectAttributes) {
		boolean respuesta = horarioService.eliminar(idhorarios);
		if (respuesta) {
			redirectAttributes.addFlashAttribute("msg_success", "Registro Eliminado");

		} else {
			redirectAttributes.addFlashAttribute("msg_error", "Eliminacion Fallida");

		}
		return "redirect:/horarios/listar";
	}

}
