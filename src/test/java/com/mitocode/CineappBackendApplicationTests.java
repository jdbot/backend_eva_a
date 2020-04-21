package com.mitocode;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mitocode.model.Cliente;
import com.mitocode.model.Usuario;
import com.mitocode.service.IUsuarioService;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class CineappBackendApplicationTests {

	@Autowired
	private IUsuarioService service;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Test
	public void crearUsuario() {
		Usuario us = new Usuario();
		us.setNombre("code");
		us.setClave(bcrypt.encode("123"));
		us.setEstado(true);

		Cliente c = new Cliente();
		c.setNombres("JAIME");
		c.setApellidos("DELGADO");
		c.setDni("72301306");
		c.setFechaNac(LocalDate.of(1991, 1, 21));
		c.setUsuario(us);
		us.setCliente(c);

		Usuario retorno = service.registrarTransaccional(us);

		assertTrue(retorno.getClave().equalsIgnoreCase(us.getClave()));
	}

}
