package com.spring.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.spring.model.Convidado;
import com.spring.model.Evento;
import com.spring.repository.IConvidadoRepository;
import com.spring.repository.IEventoRepository;

@Controller
public class EventoController {
	
	@Autowired
	private IEventoRepository er;
	
	@Autowired
	private IConvidadoRepository cr;

	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}
	
	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
	public RedirectView form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("erro", "Verifique os campos.");
			return new RedirectView("/cadastrarEvento");
		}
		er.save(evento);
		attributes.addFlashAttribute("sucesso", "Evento cadastrado com sucesso!");
		return new RedirectView("/cadastrarEvento");
	}
	
	@RequestMapping(value = "/eventos", method = RequestMethod.GET)
	public ModelAndView listaDeEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos);
		return mv;
	}
	
	@RequestMapping(value = "/detalhesEvento/{id}")
	public ModelAndView detalheEvento(@PathVariable("id") long id) {
		Evento evento = er.findById(id);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		
		Iterable<Convidado> convidados  = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
		
		return mv;
	}
	
	@RequestMapping(value = "/detalhesEvento/{id}", method = RequestMethod.POST)
	public RedirectView detalheEvento(@PathVariable("id") long id, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("erro", "Verifique os campos.");
			attributes.addFlashAttribute("convidado",convidado);
			return new RedirectView("/detalhesEvento/{id}");
		}
		Evento evento = er.findById(id);
		convidado.setEvento(evento);
		cr.save(convidado);
		attributes.addFlashAttribute("sucesso", "Convidado adicionado com sucesso!");
		return new RedirectView("/detalhesEvento/{id}");
	}
	
	@RequestMapping(value = "/deletarEvento/{id}", method = RequestMethod.GET)
	public RedirectView deletarEvento(@PathVariable("id") long id) {
		Evento evento = er.findById(id);
		er.delete(evento);
		return new RedirectView("/eventos");
	}
	
	@RequestMapping(value = "/{rg}")
	public RedirectView deletarConvidado(@PathVariable("rg") String rg) {
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);
		
		return new RedirectView("/detalhesEvento/"+convidado.getEvento().getId());
	}
}