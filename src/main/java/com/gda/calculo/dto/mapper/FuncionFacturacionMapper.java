/**
 * 
 */
package com.gda.calculo.dto.mapper;

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
		FuncionFacturacionDto dto = new FuncionFacturacionDto();
		dto.setKordensucursal(rs.getInt(column++));
		dto.setKordensucursalfac(rs.getInt(column++));
		dto.setKpaciente(rs.getInt(column++));
		dto.setCconvenio(rs.getInt(column++));
		dto.setSnobrepaciente(rs.getString(column++));
		dto.setCexamen(rs.getInt(column++));
		dto.setSexamen(rs.getString(column++));
		dto.setMsubtotal(rs.getBigDecimal(column++));
		dto.setMiva(rs.getBigDecimal(column++));
		dto.setMtotal(rs.getBigDecimal(column++));
		//dto.setTotalFactura(rs.getString(column++));
		column++;
		dto.setUconsecutivo(rs.getInt(column++));
		dto.setDregistro(rs.getDate(column++));
		dto.setSclasificacioncomercial(rs.getString(column++));
		return dto;
	}

}
