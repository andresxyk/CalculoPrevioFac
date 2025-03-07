/**
 * 
 */
package com.gda.calculo.dto.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gda.calculo.dto.DatosAdicionalesDto;

/**
 * 
 */
public class DatosAdicionalesMapper implements RowMapper<DatosAdicionalesDto> {

	@Override
	public DatosAdicionalesDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer index=1;
		DatosAdicionalesDto dto = new DatosAdicionalesDto();
		dto.setSdatoadicional1(rs.getString(index++));
		dto.setSdatoadicional2(rs.getString(index++));
		dto.setSdatoadicional3(rs.getString(index++));
		dto.setSdatoadicional4(rs.getString(index++));
		dto.setSdatoadicional5(rs.getString(index++));
		/*dto.setSdatoadicional6(rs.getString(index++));
		dto.setSdatoadicional7(rs.getString(index++));
		dto.setSdatoadicional8(rs.getString(index++));
		dto.setSdatoadicional9(rs.getString(index++));
		dto.setSdatoadicional10(rs.getString(index++));*/
		return dto;
	}

}
