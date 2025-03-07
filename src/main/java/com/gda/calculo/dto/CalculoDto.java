/**
 * 
 */
package com.gda.calculo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class CalculoDto extends DatosAdicionalesDto {

	private Long consecutivo;
	private Long consecutivoFac;
	private String kPaciente;
	private String noConvenio;
	private String nombrePaciente;
	private String codigoEstudio;
	private String nombreEstudio;
	private String costoUnitario;
	private String mIva;
	private String mTotal;
	private String totalFactura;
	private String bloque;
	private String fechaOrden;
	private String sClasificacionComercial;
}
