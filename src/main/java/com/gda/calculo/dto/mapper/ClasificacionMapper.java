/**
 * 
 */
package com.gda.calculo.dto.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gda.calculo.dto.ClasificacionDto;

/**
 * 
 */
public class ClasificacionMapper implements RowMapper<ClasificacionDto> {

	@Override
	public ClasificacionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		ClasificacionDto dto = new ClasificacionDto();
		int index=1;
		dto.setCExamen(rs.getString(index++));
		dto.setCCliente(rs.getString(index++));
		dto.setCConvenio(rs.getString(index++));
		dto.setSclasificacioncliente(rs.getString(index++));
		return dto;
	}

}
