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
public class FuncionFacturacionMapper implements RowMapper<FuncionFacturacionDto> {

	@Override
	public FuncionFacturacionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer column = 1;
		BigDecimal subtotal = new BigDecimal(0);
		BigDecimal iva = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		
		FuncionFacturacionDto dto = new FuncionFacturacionDto();
		dto.setKordensucursal(rs.getInt(column++));
		dto.setKordensucursalfac(rs.getInt(column++));
		dto.setKpaciente(rs.getInt(column++));
		dto.setCconvenio(rs.getInt(column++));
		dto.setSnobrepaciente(rs.getString(column++));
		dto.setCexamen(rs.getInt(column++));
		dto.setSexamen(rs.getString(column++));
		
		subtotal = rs.getBigDecimal(column++);
		subtotal = subtotal.divide(BigDecimal.valueOf(1.16), 2, RoundingMode.HALF_UP);
		dto.setMsubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
		
		iva = rs.getBigDecimal(column++);
		iva = iva.divide(BigDecimal.valueOf(1.16),2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0.16));
		dto.setMiva(iva.setScale(2, RoundingMode.HALF_UP));
		
		total = subtotal.add(iva);
		dto.setMtotal(total.setScale(2, RoundingMode.HALF_UP));
		column++;
		
		//dto.setTotalFactura(rs.getString(column++));
		column++;
		dto.setUconsecutivo(rs.getInt(column++));
		dto.setDregistro(rs.getDate(column++));
		dto.setSclasificacioncomercial(rs.getString(column++));
		return dto;
	}

}
