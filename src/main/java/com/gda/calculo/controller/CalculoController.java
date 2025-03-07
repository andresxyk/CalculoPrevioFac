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

import com.gda.calculo.dto.CalculoDto;
import com.gda.calculo.service.CalculosService;


/**
 * 
 */
@RestController
public class CalculoController {
	
	@Autowired
	CalculosService service;
	
	@GetMapping("/api/calculo/{pconvenio}/{uconsecutivo}")
	public ResponseEntity<List<CalculoDto>> getCalculos(@PathVariable Long pconvenio, @PathVariable String uconsecutivo) {
		
		return ResponseEntity.ok(service.getCalculos(pconvenio, uconsecutivo));
	}
	

}
