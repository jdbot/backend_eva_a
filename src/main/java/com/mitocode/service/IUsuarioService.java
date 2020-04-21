package com.mitocode.service;

import com.mitocode.model.Usuario;

public interface IUsuarioService{

	Usuario registrarTransaccional(Usuario us);
	
	Integer obtenerIDUsuario(String nombre);
}
