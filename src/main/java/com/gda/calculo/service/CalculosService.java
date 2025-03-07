/**
 * 
 */
package com.gda.calculo.service;

import java.util.List;

import com.gda.calculo.dto.CalculoDto;

/**
 * 
 */
public interface CalculosService {

	public List<CalculoDto> getCalculos(Long pCconvenio, String pUconsecutivo);
	
	public List<CalculoDto> getDatosAdicionales(Long pCconvenio, String pUconsecutivo, List<CalculoDto> lstCalculos);
	
	void actualizaciones(Long pCconvenio, String pUconsecutivo, Boolean pBdefinitivo, Integer pUserid, Integer pMaxamount, List<CalculoDto> lstCalculos); 
}
