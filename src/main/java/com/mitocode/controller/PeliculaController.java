package com.mitocode.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Pelicula;
import com.mitocode.service.IPeliculaService;


@RestController
@RequestMapping("/peliculas")
public class PeliculaController {
	
	@Autowired
	private IPeliculaService service;
	
	//@PreAuthorize("@restAuthServiceImpl.tieneAcceso('listar')")
	//@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Pelicula>> listar(){
		List<Pelicula> lista = service.listar();
		return new ResponseEntity<List<Pelicula>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Pelicula> listarPorId(@PathVariable("id") Integer id) {
		Pelicula pel = service.listarPorId(id);
		if(pel.getIdPelicula() == null) {
			throw new ModeloNotFoundException("ID NO EXISTE: " + id);
		}
		return new ResponseEntity<Pelicula>(pel, HttpStatus.OK);
	}
	
	//Spring Boot 2.1 | Hateoas 0.9
		/*@GetMapping(value = "/{id}")
		public Resource<Pelicula> listarPorId(@PathVariable("id") Integer id){
			
			Pelicula pel = service.listarPorId(id);
			if(pel.getIdPelicula() == null) {
				throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
			}
			
			Resource<Pelicula> resource = new Resource<Pelicula>(pel);
			// /peliculas/{4}
			ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
			resource.add(linkTo.withRel("pelicula-resource"));
			
			return resource;
		}*/
	
	@GetMapping("/hateoas/{id}")
	//Spring Boot 2.2 | Hateoas 1
	public EntityModel<Pelicula> listarPorIdHateoas(@PathVariable("id") Integer id){
		
		Pelicula pel = service.listarPorId(id);
		if(pel.getIdPelicula() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		EntityModel<Pelicula> resource = new EntityModel<Pelicula>(pel);
		// /peliculas/{4}
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("pelicula-resource"));
		
		return resource;
	}
	
	/*@PostMapping
	private ResponseEntity<Pelicula> registrar(@Valid @RequestBody Pelicula obj) {
		Pelicula pel = service.registrar(obj);
		return new ResponseEntity<Pelicula>(pel, HttpStatus.CREATED);
	}*/
	
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Pelicula obj) {
		Pelicula pel = service.registrar(obj);
		
		// localhost:8080/peliculas/2
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pel.getIdPelicula()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Pelicula>  modificar(@Valid @RequestBody Pelicula obj) {
		Pelicula pel = service.modificar(obj);
		return new ResponseEntity<Pelicula>(pel, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		Pelicula pel = service.listarPorId(id);
		if(pel.getIdPelicula() == null) {
			throw new ModeloNotFoundException("ID NO EXISTE: " + id);
		}
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

}
