package com.mitocode.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Cliente;
import com.mitocode.model.Usuario;
import com.mitocode.service.IClienteService;


@RestController
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private IClienteService service;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@GetMapping
	public ResponseEntity<List<Cliente>> listar(){
		List<Cliente> lista = service.listar();
		return new ResponseEntity<List<Cliente>>(lista, HttpStatus.OK);
	}
	
	/*@GetMapping("/{id}")
	public ResponseEntity<Cliente> listarPorId(@PathVariable("id") Integer id) {
		Cliente pel = service.listarPorId(id);
		if(pel.getIdCliente() == null) {
			throw new ModeloNotFoundException("ID NO EXISTE: " + id);
		}
		return new ResponseEntity<Cliente>(pel, HttpStatus.OK);
	}*/
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> listarPorId(@PathVariable("id") Integer id) {
		Cliente pel = service.listarPorId(id);
		byte[] data = pel.getFoto();
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
	//Spring Boot 2.1 | Hateoas 0.9
		/*@GetMapping(value = "/{id}")
		public Resource<Cliente> listarPorId(@PathVariable("id") Integer id){
			
			Cliente pel = service.listarPorId(id);
			if(pel.getIdCliente() == null) {
				throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
			}
			
			Resource<Cliente> resource = new Resource<Cliente>(pel);
			// /Clientes/{4}
			ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
			resource.add(linkTo.withRel("Cliente-resource"));
			
			return resource;
		}*/
	
	@GetMapping("/hateoas/{id}")
	//Spring Boot 2.2 | Hateoas 1
	public EntityModel<Cliente> listarPorIdHateoas(@PathVariable("id") Integer id){
		
		Cliente pel = service.listarPorId(id);
		if(pel.getIdCliente() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		EntityModel<Cliente> resource = new EntityModel<Cliente>(pel);
		// /Clientes/{4}
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("Cliente-resource"));
		
		return resource;
	}
	
	/*@PostMapping
	private ResponseEntity<Cliente> registrar(@Valid @RequestBody Cliente obj) {
		Cliente pel = service.registrar(obj);
		return new ResponseEntity<Cliente>(pel, HttpStatus.CREATED);
	}*/
	
	/*@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Cliente obj) {
		Cliente pel = service.registrar(obj);
		
		// localhost:8080/Clientes/2
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pel.getIdCliente()).toUri();
		return ResponseEntity.created(location).build();
	}*/
	
	@PostMapping
	public Cliente registrar(@RequestPart("cliente") Cliente cliente, @RequestPart("file") MultipartFile file)
			throws IOException {
		Cliente c = cliente;			
		c.setFoto(file.getBytes());
		Usuario u = new Usuario();
		u.setEstado(true);
		u.setIdUsuario(c.getIdCliente());
		String nombreUsuario = c.getNombres().substring(0,1);
		String[] apellidos = c.getApellidos().split(" ");
		nombreUsuario = nombreUsuario + apellidos[0];
		u.setNombre(nombreUsuario.toLowerCase());
		u.setClave(bcrypt.encode("123"));
		u.setCliente(c);
		c.setUsuario(u);
		return service.registrar(c);
	}

	
	/*@PutMapping
	public ResponseEntity<Cliente>  modificar(@Valid @RequestBody Cliente obj) {
		Cliente pel = service.modificar(obj);
		return new ResponseEntity<Cliente>(pel, HttpStatus.OK);
	}*/
	
	@PutMapping
	public Cliente modificar(@RequestPart("cliente") Cliente cliente, @RequestPart("file") MultipartFile file)
			throws IOException {
		Cliente c = cliente;		
		c.setFoto(file.getBytes());
		return service.modificar(c);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		Cliente pel = service.listarPorId(id);
		if(pel.getIdCliente() == null) {
			throw new ModeloNotFoundException("ID NO EXISTE: " + id);
		}
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	

}
