/**
 * 
 */
package com.gda.calculo.service.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.gda.calculo.dto.CalculoDto;
import com.gda.calculo.dto.ClasificacionDto;
import com.gda.calculo.dto.DatosAdicionalesDto;
import com.gda.calculo.dto.mapper.CalculoMapper;
import com.gda.calculo.dto.mapper.ClasificacionMapper;
import com.gda.calculo.dto.mapper.DatosAdicionalesMapper;
import com.gda.calculo.service.CalculosService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@Service
public class CalculosServiceImpl extends JdbcDaoSupport implements CalculosService {

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Autowired
	DataSource dataSource;

	@Override
	public List<CalculoDto> getCalculos(Long pCconvenio, String pUconsecutivo) {
		String var1, strsql;
		List<CalculoDto> list = null;
		if (pCconvenio != 309 && pCconvenio != 310 && pCconvenio != 311 && pCconvenio != 312)
			var1 = " and tosf.cconvenio = " + (pCconvenio);
		else
			var1 = " and tosf.cconvenio in (309,310,311,312)";

		strsql = "select tosf.kordensucursal as Consecutivo, tosf.kordensucursalfac as Consecutivofac,	tp.kpaciente, "
				+ "tosf.cconvenio as NoConvenio,	"
				+ "tp.sapellidopaterno||' '||tp.sapellidomaterno||' '||tp.snombre as NombrePaciente,	"
				+ "toesf.cexamen as CodigoEstudio,	                                                                                    "
				+ "				toesf.sexamen as NombreEstudio,	                                                                        "
				+ "				round (sum((toesf.mfacturaempresa/1.16)),2) costo_unitario,                                             "
				+ "				round (sum((toesf.mfacturaempresa/1.16)),2)*.16 miva,                                                   "
				+ "				round (sum((toesf.mfacturaempresa/1.16)),2)+ (round (sum((toesf.mfacturaempresa/1.16)),2)*.16) mtotal,  "
				+ "				sum (toesf.mfacturaempresa) as TotalFactura,	                                                        "
				+ "				tosf.uconsecutivo as Bloque,	                                                                        "
				+ "				to_char(tos.dregistro,'dd-mm-yyyy') as FechaOrden, ccc.sclasificacioncomercial	                    "
				+ "			from	t_orden_sucursal tos,t_orden_sucursal_fac tosf,                                                     "
				+ "				t_orden_examen_sucursal_fac toesf,                                                                      "
				+ "				c_examen ce, c_convenio cc,c_cliente ccl, t_paciente tp	, c_clasificacion_comercial ccc                 "
				+ "			where	toesf.kordensucursal = tosf.kordensucursal	                                                        "
				+ "				and tos.kordensucursal = tosf.kordensucursal	                                                        "
				+ "				and tp.kpaciente = tos.kpaciente	                                                                    "
				+ "				and toesf.cexamen = ce.cexamen                                                                          "
				+ "				AND ce.cclasificacioncomercial =ccc.cclasificacioncomercial	                                            "
				+ "				and cc.cconvenio  = tosf.cconvenio	                                                                    "
				+ "				and ccl.ccliente = cc.ccliente	                                                                        "
				+ "				AND tosf.csucursal <> 100	                                                                            "
				+ "				AND CTIPOCONVENIO = 22	                                                                                "
				+ "				and tosf.cestadoregistro = 37	                                                                        "
				+ "				and uconsecutivo in (" + pUconsecutivo
				+ ")                                                                 "
				+ "				and toesf.cperfil = -1	                                                                                "
				+ "				and toesf.cestadoregistro <> 43" + var1
				+ " and toesf.kordensucursalfac = tosf.kordensucursalfac	    "
				+ "			group by tosf.kordensucursal, tosf.kordensucursalfac, tp.kpaciente, tosf.uorden,cc.ccliente, tosf.cconvenio ,cc.sconvenio, toesf.cexamen,toesf.sexamen, ccl.srazonsocial, "
				+ "			tp.sapellidopaterno, tp.sapellidomaterno, tp.snombre,tos.sobservacion,tosf.dregistro,tosf.uconsecutivo,tos.dregistro, sclasificacioncomercial "
				+ "	UNION ALL																									"
				+ "			select	tosf.kordensucursal as Consecutivo, tosf.kordensucursalfac as Consecutivofac, tp.kpaciente,	        "
				+ "				tosf.cconvenio as NoConvenio,	                                                                        "
				+ "				tp.sapellidopaterno||' '||tp.sapellidomaterno||' '||tp.snombre as NombrePaciente, 	                "
				+ "				toesf.cperfil as CodigoEstudio,	                                                                        "
				+ "				toesf.sperfil as NombreEstudio,                                                                         "
				+ "				round (sum((toesf.mfacturaempresa/1.16)),2) costo_unitario,                                             "
				+ "				round (sum((toesf.mfacturaempresa/1.16)),2)*.16 miva,                                                   "
				+ "				round (sum((toesf.mfacturaempresa/1.16)),2)+ (round (sum((toesf.mfacturaempresa/1.16)),2)*.16) mtotal,  "
				+ "				sum(toesf.mfacturaempresa) as TotalFactura,	                                                            "
				+ "				tosf.uconsecutivo as Bloque,	                                                                        "
				+ "				to_char(tos.dregistro,'dd-mm-yyyy') as FechaOrden, 'LABORATORIO' as sclasificacioncomercial	        "
				+ "			from 	t_orden_sucursal tos,t_orden_sucursal_fac tosf, t_orden_examen_sucursal_fac toesf, c_convenio cc, c_cliente ccl, 	"
				+ "				t_paciente tp, c_perfil cp																				   "
				+ "			where	toesf.kordensucursal = tosf.kordensucursal 	                                                           "
				+ "				and tos.kordensucursal = tosf.kordensucursal	                                                           "
				+ "				and tp.kpaciente = tos.kpaciente	                                                                       "
				+ "				and toesf.cperfil = cp.cperfil	                                                                           "
				+ "				and cc.cconvenio  = tosf.cconvenio	                                                                       "
				+ "				and ccl.ccliente = cc.ccliente	                                                                           "
				+ "				AND tosf.csucursal <> 100	                                                                               "
				+ "				AND CTIPOCONVENIO = 22	                                                                                   "
				+ "				and tosf.cestadoregistro = 37	                                                                           "
				+ "				and toesf.cperfil <> -1	                                                                                   "
				+ "				and toesf.cestadoregistro <> 43                                                                            "
				+ "				and uconsecutivo in (" + pUconsecutivo + ") " + var1
				+ " and toesf.kordensucursalfac = tosf.kordensucursalfac "
				+ "			group by	tosf.kordensucursal, tosf.kordensucursalfac , tp.kpaciente, tosf.uorden,cc.ccliente, tosf.cconvenio ,cc.sconvenio, toesf.cperfil ,toesf.sperfil , ccl.srazonsocial,	"
				+ "				tp.sapellidopaterno, tp.sapellidomaterno, tp.snombre, tos.sobservacion,tosf.dregistro,tosf.uconsecutivo,tos.dregistro, sclasificacioncomercial	"
				+ "			ORDER BY 1 ,6";
		list = this.getJdbcTemplate().query(strsql, new CalculoMapper());
		log.info("Registros obtenidos " + list.size());
		getDatosAdicionales(pCconvenio, pUconsecutivo, list);
		return list;
	}

	@Override
	public List<CalculoDto> getDatosAdicionales(Long pCconvenio, String pUconsecutivo, List<CalculoDto> lstCalculos) {
		List<DatosAdicionalesDto> list = null;
		String var1, var2, var3, strsql;

		if (pCconvenio != 309 && pCconvenio != 310 && pCconvenio != 311 && pCconvenio != 312) {
			var1 = "cconvenio = " + (pCconvenio);
			var3 = "kordensucursal";
		} else {
			var1 = "cconvenio in (309,310,311,312) ";
			var3 = "kpaciente";

		}

		for (CalculoDto calculoDto : lstCalculos) {
			if (var3.equals("kordensucursal")) {
				var2 = " select distinct lower(trim (translate (sdatoadicional, '. ', '_'))) as nombrecampo, cda.cdatoadicional, tda.kordensucursal as llave, translate (tda.svalor, '''', ' ') svalor, cconvenio "
						+ " from t_dato_adicional tda,  c_dato_adicional cda where tda.cdatoadicional = cda.cdatoadicional and cda.cconvenio = "
						+ (pCconvenio) + " and tda.kordensucursal in ( " + calculoDto.getConsecutivo() + " )";

				list = this.getJdbcTemplate().query(var2, new DatosAdicionalesMapper());
				int j=1;
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).getSdatoadicional4().trim().equals("")) {
						switch (j) {
						case 1:
							calculoDto.setSdatoadicional1(list.get(i).getSdatoadicional4());
							break;
						case 2:
							calculoDto.setSdatoadicional2(list.get(i).getSdatoadicional4());
							break;
						case 3:
							calculoDto.setSdatoadicional3(list.get(i).getSdatoadicional4());
							break;
						case 4:
							calculoDto.setSdatoadicional4(list.get(i).getSdatoadicional4());
							break;
						case 5:
							calculoDto.setSdatoadicional5(list.get(i).getSdatoadicional4());
							break;
						case 6:
							calculoDto.setSdatoadicional6(list.get(i).getSdatoadicional4());
							break;
						case 7:
							calculoDto.setSdatoadicional7(list.get(i).getSdatoadicional4());
							break;
						case 8:
							calculoDto.setSdatoadicional8(list.get(i).getSdatoadicional4());
							break;
						case 9:
							calculoDto.setSdatoadicional9(list.get(i).getSdatoadicional4());
							break;
						case 10:
							calculoDto.setSdatoadicional10(list.get(i).getSdatoadicional4());
							break;
						}
						j++;
					}
				}
			}
			if (var3.equals("kpaciente")) {
				var2 = " select distinct lower(trim (translate (sdatoadicional, '. ', '_'))) as nombrecampo, cda.cdatoadicional, tda.kpaciente as llave, translate (tda.svalor, '''', ' ') svalor, cconvenio "
						+ " from t_dato_adicional tda,  c_dato_adicional cda  where tda.cdatoadicional = cda.cdatoadicional and cda.cconvenio in (309,310,311,312) and tda.kpaciente in ( "
						+ calculoDto.getKPaciente() + " ) ";
				list = this.getJdbcTemplate().query(var2, new DatosAdicionalesMapper());
				int j=1;
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).getSdatoadicional4().trim().equals("")) {
						switch (j) {
						case 1:
							calculoDto.setSdatoadicional1(list.get(i).getSdatoadicional4());
							break;
						case 2:
							calculoDto.setSdatoadicional2(list.get(i).getSdatoadicional4());
							break;
						case 3:
							calculoDto.setSdatoadicional3(list.get(i).getSdatoadicional4());
							break;
						case 4:
							calculoDto.setSdatoadicional4(list.get(i).getSdatoadicional4());
							break;
						case 5:
							calculoDto.setSdatoadicional5(list.get(i).getSdatoadicional4());
							break;
						case 6:
							calculoDto.setSdatoadicional6(list.get(i).getSdatoadicional4());
							break;
						case 7:
							calculoDto.setSdatoadicional7(list.get(i).getSdatoadicional4());
							break;
						case 8:
							calculoDto.setSdatoadicional8(list.get(i).getSdatoadicional4());
							break;
						case 9:
							calculoDto.setSdatoadicional9(list.get(i).getSdatoadicional4());
							break;
						case 10:
							calculoDto.setSdatoadicional10(list.get(i).getSdatoadicional4());
							break;
						}
						j++;
					}
				}
			}
			strsql = "select distinct(ccc.cexamen) cexamen, ccc.ccliente, cc.cconvenio, sclasificacioncliente "
					+ "		from c_clasificacion_cliente ccc,  c_convenio cc "
					+ "		where ccc.ccliente= cc.ccliente " + "		and cc." + var1 + "     and ccc.cexamen in ("
					+ calculoDto.getCodigoEstudio() + ")";
			List<ClasificacionDto> lstClasificacion = null;
			lstClasificacion = this.getJdbcTemplate().query(strsql, new ClasificacionMapper());
			if (lstClasificacion.size() > 0)
				calculoDto.setSClasificacionComercial(lstClasificacion.get(0).getSclasificacioncliente());
		}

		return lstCalculos;
	}

	@Override
	public void actualizaciones(Long pCconvenio, String pUconsecutivo, Boolean pBdefinitivo, Integer pUserid,
			Integer pMaxamount, List<CalculoDto> lstCalculos) {
		String strsql;
		if( pBdefinitivo = true  && pMaxamount == -1  && pCconvenio != 309 && pCconvenio != 310 && pCconvenio != 311 && pCconvenio != 312) { 
				log.info("entre global");

				strsql = "select ufolioactual into v_folioactual from c_control_folio where sserie = 'A' AND CSUCURSAL = 1003 AND CESTADOREGISTRO=31";
				String vFolioActual = this.getJdbcTemplate().queryForObject(strsql, String.class);
				

				strsql = "select decode (sum (msubtotal),null, -1,sum (msubtotal))::numeric validaactualizaciones, '||'''insert into t_factura values(t_factura_sequence.nextval,(select kdatofiscal from c_convenio_dato_fiscal where cconvenio = " + pCconvenio + ")'||'"+
				",''''EMPRESAS'''',"+vFolioActual+1+",(select ccliente from c_convenio  where cconvenio ='"+pCconvenio+"')'||',1003,1,'''"+
				"||'||sum (msubtotal)||'''"+
				"||',0.00,0.00,'''"+
				"||'||sum (miva)||'','''"+
				"||'||sum (mtotal)||'''"+
				"||',1, " + pCconvenio + ", '''' '''', ''''  '''', 1,sysdate,33,sysdate,"+ pUserid +","+pUserid+", '''' '''' ,'''' '''' ,  '''' '''', ''''A'''');"+
				"'' as inserta  , "+

				"''update c_control_folio set ufolioactual = "+vFolioActual+1+" where csucursal = 1003 and cestadoregistro=31 and sserie=''''A'''';'' as updfolio "+

				",''update t_orden_sucursal_fac set kfactura = (select kfactura from t_factura where sserie=''''A'''' and csucursal = 1003 and ufoliofactura = "+vFolioActual+1+")"+
				", cestadoregistro= 35 where cconvenio = " + pCconvenio + " and cestadoregistro = 37 and kordensucursalfac in (select distinct (kordensucursalfac) from facturacion.'|| p_nombretabla ||'"+
				"where uconsecutivo in (" + pUconsecutivo + ") and cconvenio =" + pCconvenio + ");'' as updfact , "+

				"''update t_orden_examen_sucursal_fac set kfactura = (select kfactura from t_factura where sserie=''''A'''' and csucursal = 1003 and ufoliofactura = " + vFolioActual + 1 + ") "+
				"where cestadoregistro <> 43 and kordensucursalfac in (select distinct (kordensucursalfac) from facturacion.'|| p_nombretabla ||'"+
				"where uconsecutivo in (" + pUconsecutivo + ") and cconvenio =" + pCconvenio + ");'' as updexafact ,"+

				"''update  facturacion.'||p_nombretabla ||' set kfactura = (select kfactura from t_factura where sserie=''''A'''' and csucursal = 1003 and ufoliofactura = " + vFolioActual + 1 + ") "+
				" , ufoliofactura = " + vFolioActual + 1 + " where kordensucursalfac in (select distinct (kordensucursalfac) from facturacion.'|| p_nombretabla ||'"+
				"where uconsecutivo in(" + pUconsecutivo + ") and cconvenio =" + pCconvenio + ");'' as updprevio "+
				
				"from facturacion.'|| p_nombretabla ||' tfb"+
				"where cconvenio = " + pCconvenio + " and uconsecutivo in (" + pUconsecutivo + ");" ;

				 log.info("SQL To Execute: "+ strsql);


			/*for registro in

			execute  strsql

			loop

				if registro.validaactualizaciones > 0 then
					log.info("SQL To Execute: %', registro.inserta;
					execute immediate registro.inserta;

					log.info("SQL To Execute: %', registro.updfolio;
					execute immediate registro.updfolio;

					log.info("SQL To Execute: %', registro.updfact;
					execute immediate registro.updfact;

					log.info("SQL To Execute: %', registro.updexafact;
					execute immediate registro.updexafact;

					log.info("SQL To Execute: %', registro.updprevio;
					execute immediate registro.updprevio;
				else 
					log.info("Solitud sin datos para el convenio: % con bloque(s): %', p_cconvenio , p_uconsecutivo;
				end if;
				
			end loop;
			
				ret :=  0; --(folio::numeric);
				return   ret;*/
		}
		
	}

}
