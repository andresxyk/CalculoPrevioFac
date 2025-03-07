/**
 * 
 */
package com.gda.calculo.dto.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gda.calculo.dto.CalculoDto;

/**
 * 
 */
public class CalculoMapper implements RowMapper<CalculoDto> {

	@Override
	public CalculoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer column = 1;
		CalculoDto dto = new CalculoDto();
		dto.setConsecutivo(rs.getLong(column++));
		dto.setConsecutivoFac(rs.getLong(column++));
		dto.setKPaciente(rs.getString(column++));
		dto.setNoConvenio(rs.getString(column++));
		dto.setNombrePaciente(rs.getString(column++));
		dto.setCodigoEstudio(rs.getString(column++));
		dto.setNombreEstudio(rs.getString(column++));
		dto.setCostoUnitario(rs.getString(column++));
		dto.setMIva(rs.getString(column++));
		dto.setMTotal(rs.getString(column++));
		dto.setTotalFactura(rs.getString(column++));
		dto.setBloque(rs.getString(column++));
		dto.setFechaOrden(rs.getString(column++));
		dto.setSClasificacionComercial(rs.getString(column++));
		return dto;
	}

}
