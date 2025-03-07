/**
 * 
 */
package com.gda.calculo.service;

import java.util.List;

import com.gda.calculo.dto.FuncionFacturacionDto;

/**
 * 
 */
public interface CalculosService {

	public List<FuncionFacturacionDto> getCalculos(Long pCconvenio, String pUconsecutivo, String ntipoprevio);
	
	public List<FuncionFacturacionDto> getDatosAdicionales(Long pCconvenio, String pUconsecutivo, List<FuncionFacturacionDto> lstCalculos);
	
	void actualizaciones(Long pCconvenio, String pUconsecutivo, Boolean pBdefinitivo, Integer pUserid, Integer pMaxamount,Integer p_razon, List<FuncionFacturacionDto> lstCalculos); 
}
