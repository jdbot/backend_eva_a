package com.mitocode.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Pelicula;
import com.mitocode.repo.IPeliculaRepo;
import com.mitocode.service.IPeliculaService;

@Service
public class PeliculaServiceImpl implements IPeliculaService{

	@Autowired
	private IPeliculaRepo repo;
	
	@Override
	public Pelicula registrar(Pelicula obj) {
		return repo.save(obj);
	}

	@Override
	public Pelicula modificar(Pelicula obj) {
		return repo.save(obj);
	}

	@Override
	public List<Pelicula> listar() {
		return repo.findAll();
	}

	@Override
	public Pelicula listarPorId(Integer v) { 
		Optional<Pelicula> op = repo.findById(v);
		return op.isPresent() ? op.get() : new Pelicula();
	}

	@Override
	public void eliminar(Integer v) {
		repo.deleteById(v);
	}

}
