/**
 * 
 */
package com.gda.calculo.dto.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gda.calculo.dto.FuncionFacturacionDto;

/**
 * 
 */
public class CalculoMapper implements RowMapper<FuncionFacturacionDto> {

	@Override
	public FuncionFacturacionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		FuncionFacturacionDto dto = new FuncionFacturacionDto();
		BigDecimal subtotal = new BigDecimal(0);
		BigDecimal iva = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		

		dto.setCconvenio(rs.getInt("noconvenio"));
		dto.setCexamen(rs.getInt("codigoestudio"));
		dto.setCantidad(rs.getString("cantidad"));
		dto.setSexamen(rs.getString("nombreestudio"));
		
		subtotal = rs.getBigDecimal("msubtotal");
		subtotal = subtotal.divide(BigDecimal.valueOf(1.16), 2, RoundingMode.HALF_UP);
		dto.setMsubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
		
		iva = rs.getBigDecimal("miva");
		iva = iva.divide(BigDecimal.valueOf(1.16),2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0.16));
		dto.setMiva(iva.setScale(2, RoundingMode.HALF_UP));
		
		total = subtotal.add(iva);
		dto.setMtotal(total.setScale(2, RoundingMode.HALF_UP));
		dto.setUconsecutivo(rs.getInt("bloque"));
		return dto;
	}

}
