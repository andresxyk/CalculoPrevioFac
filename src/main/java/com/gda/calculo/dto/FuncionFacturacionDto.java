package com.gda.calculo.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class FuncionFacturacionDto {

	private Integer ufoliofactura;
	private Integer kfactura;
	private Integer kordensucursal;
	private Integer kordensucursalfac;
	private Integer kpaciente;
	private Integer cconvenio;
	private String snobrepaciente;
	private Integer cexamen;
	private String sexamen;
	private String cantidad;
	private BigDecimal msubtotal;
	private BigDecimal miva;
	private BigDecimal mtotal;
	private Integer uconsecutivo;
	private Date dregistro;
	private String sclasificacioncomercial;
	private String sdatoadicional1;
	private String sdatoadicional2;
	private String sdatoadicional3;
	private String sdatoadicional4;
	private String sdatoadicional5;
	private String sdatoadicional6;
	private String sdatoadicional7;
	private String sdatoadicional8;
	private String sdatoadicional9;
	private String sdatoadicional10;
	
}
