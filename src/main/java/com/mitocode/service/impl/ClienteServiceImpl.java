package com.mitocode.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mitocode.model.Cliente;
import com.mitocode.model.Usuario;
import com.mitocode.repo.IClienteRepo;
import com.mitocode.service.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService{

	@Autowired
	private IClienteRepo repo;
	
	@Value("${mitocine.default-rol}")
	private Integer DEFAULT_ROL;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Transactional
	@Override
	public Cliente registrar(Cliente obj) {
		Cliente c;
		try {
			c = repo.save(obj);
			repo.registrarRolPorDefecto(c.getIdCliente(), DEFAULT_ROL);
		}catch(Exception e) {
			throw e;
		}
		return c;
	}
	
	@Transactional
	@Override
	public Cliente modificar(Cliente obj) {
		if(obj.getFoto().length > 0) {
			repo.modificarFoto(obj.getIdCliente(), obj.getFoto());			
		}	
		return repo.save(obj);
	}

	@Override
	public List<Cliente> listar() {
		return repo.findAll();
	}

	@Override
	public Cliente listarPorId(Integer v) { 
		Optional<Cliente> op = repo.findById(v);
		return op.isPresent() ? op.get() : new Cliente();
	}

	@Override
	public void eliminar(Integer v) {
		repo.deleteById(v);
	}

}
