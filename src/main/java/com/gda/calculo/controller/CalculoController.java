/**
 * 
 */
package com.gda.calculo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.gda.calculo.dto.FuncionFacturacionDto;
import com.gda.calculo.service.CalculosService;


/**
 * 
 */
@RestController
public class CalculoController {
	
	@Autowired
	CalculosService service;
	
	@GetMapping("/api/calculo/previo/{pconvenio}/{uconsecutivo}")
	public ResponseEntity<List<FuncionFacturacionDto>> getCalculos(@PathVariable Long pconvenio, @PathVariable String uconsecutivo) {
		
		return ResponseEntity.ok(service.getCalculos(pconvenio, uconsecutivo));
	}
	
	
	@GetMapping("/api/calculo/definitivo/{pconvenio}/{uconsecutivo}/{pbdefinitivo}/{puserid}/{pmaxamount}")
	public ResponseEntity<List<FuncionFacturacionDto>> getCalculos(@PathVariable Long pconvenio, @PathVariable String uconsecutivo, @PathVariable Boolean pbdefinitivo, @PathVariable Integer puserid,
			@PathVariable Integer pmaxamount) {
		List<FuncionFacturacionDto> list = service.getCalculos(pconvenio, uconsecutivo);
		service.actualizaciones(pconvenio, uconsecutivo, pbdefinitivo, puserid, pmaxamount, list);
		return ResponseEntity.ok(list);
	}
	

}
