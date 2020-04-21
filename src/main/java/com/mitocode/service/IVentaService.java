package com.mitocode.service;

import java.util.List;

import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.dto.ResumenVentaDTO;
import com.mitocode.dto.VentaDTO;
import com.mitocode.model.Venta;
import com.mitocode.model.VentaComida;

public interface IVentaService extends ICRUD<Venta, Integer>{
	
	Venta registrarTransaccional(VentaDTO ventaDto);
	byte[] generarReporte(VentaDTO venta);
	List<ResumenVentaDTO> listarResumen();
	List<Venta> buscar(FiltroConsultaDTO filtro);
	List<Venta> buscarfecha(FiltroConsultaDTO filtro);
	List<VentaComida> listarComidasPorVenta(Integer idVenta);

}
