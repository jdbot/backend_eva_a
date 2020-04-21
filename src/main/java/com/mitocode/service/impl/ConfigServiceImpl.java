package com.mitocode.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Config;
import com.mitocode.repo.IConfigRepo;
import com.mitocode.service.IConfigService;

@Service
public class ConfigServiceImpl implements IConfigService {

	@Autowired
	private IConfigRepo repo;

	@Override
	public Config registrar(Config obj) {
		return repo.save(obj);
	}

	@Override
	public Config modificar(Config obj) {
		return repo.save(obj);
	}

	@Override
	public List<Config> listar() {
		return repo.findAll();
	}

	@Override
	public Config listarPorId(Integer id) {
		Optional<Config> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Config();
	}
	
	@Override
	public Config leerParametro(String param) {
		return repo.findByParametro(param);
	}

	@Override
	public void eliminar(Integer id) {
		repo.deleteById(id);
	}

}
