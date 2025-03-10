/**
 * 
 */
package com.gda.calculo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import com.gda.calculo.dto.ClasificacionDto;
import com.gda.calculo.dto.DatosAdicionalesDto;
import com.gda.calculo.dto.FuncionFacturacionDto;
import com.gda.calculo.dto.mapper.CalculoMapper;
import com.gda.calculo.dto.mapper.ClasificacionMapper;
import com.gda.calculo.dto.mapper.DatosAdicionalesMapper;
import com.gda.calculo.dto.mapper.FuncionFacturacionMapper;
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
	public List<FuncionFacturacionDto> getCalculos(Long pCconvenio, String pUconsecutivo, String ntipoprevio) {
		String var1, strsql;
		String opc = "";
		String select = "";

		if (ntipoprevio.equals("1")) {
			select = " consecutivo,consecutivofac,kpaciente,noconvenio,nombrepaciente,codigoestudio,nombreestudio,costo_unitario,miva,mtotal,totalfactura,bloque,fechaorden,sclasificacioncomercial,ufoliofactura,kfactura ";
			opc = " order by Bloque, " + "	Consecutivo, " + "	CodigoEstudio;";
		} else if (ntipoprevio.equals("2")) {
		} else if (ntipoprevio.equals("3")) {
			select = " ufoliofactura, kfactura , NoConvenio, CodigoEstudio, count (CodigoEstudio) cantidad, NombreEstudio, sum (costo_unitario) msubtotal, sum(miva) miva , sum(mtotal) mtotal, Bloque ";
			opc = "  group by ufoliofactura, kfactura , NoConvenio, CodigoEstudio, NombreEstudio, Bloque order by NoConvenio, CodigoEstudio;";
		}

		List<FuncionFacturacionDto> list = null;
		if (pCconvenio != 309 && pCconvenio != 310 && pCconvenio != 311 && pCconvenio != 312)
			var1 = " and tosf.cconvenio = " + (pCconvenio);
		else
			var1 = " and tosf.cconvenio in (309,310,311,312)";

		strsql = "select " + select + " from ("
				+ "select tosf.kordensucursal as Consecutivo, tosf.kordensucursalfac as Consecutivofac,	tp.kpaciente, "
				+ "tosf.cconvenio as NoConvenio,	"
				+ "tp.sapellidopaterno||' '||tp.sapellidomaterno||' '||tp.snombre as NombrePaciente,	"
				+ "toesf.cexamen as CodigoEstudio,	                                                                                    "
				+ "				toesf.sexamen as NombreEstudio,	                                                                        "
				+ "				 sum(toesf.mfacturaempresa) costo_unitario,                                             "
				+ "				 sum(toesf.mfacturaempresa) miva,                                                   "
				+ "				 sum(toesf.mfacturaempresa) mtotal,  "
				+ "				sum (toesf.mfacturaempresa) as TotalFactura,	                                                        "
				+ "				tosf.uconsecutivo as Bloque,	                                                                        "
				+ "				to_char(tos.dregistro,'dd-mm-yyyy') as FechaOrden, ccc.sclasificacioncomercial, '0' as ufoliofactura, '0' as kfactura                  "
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
				+ "			tp.sapellidopaterno, tp.sapellidomaterno, tp.snombre,tos.sobservacion,tosf.dregistro,tosf.uconsecutivo,tos.dregistro, sclasificacioncomercial"
				+ "	UNION ALL																									"
				+ "			select	tosf.kordensucursal as Consecutivo, tosf.kordensucursalfac as Consecutivofac, tp.kpaciente,	        "
				+ "				tosf.cconvenio as NoConvenio,	                                                                        "
				+ "				tp.sapellidopaterno||' '||tp.sapellidomaterno||' '||tp.snombre as NombrePaciente, 	                "
				+ "				toesf.cperfil as CodigoEstudio,	                                                                        "
				+ "				toesf.sperfil as NombreEstudio,                                                                         "
				+ "				 sum(toesf.mfacturaempresa) costo_unitario,                                            					"
				+ "				 sum(toesf.mfacturaempresa) miva,                                                   					"
				+ "				 sum(toesf.mfacturaempresa) mtotal,  "
				+ "				sum(toesf.mfacturaempresa) as TotalFactura,	                                                            "
				+ "				tosf.uconsecutivo as Bloque,	                                                                        "
				+ "				to_char(tos.dregistro,'dd-mm-yyyy') as FechaOrden, 'LABORATORIO' as sclasificacioncomercial, '0' as ufoliofactura, '0' as kfactura 	        "
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
				+ "			ORDER BY 1 ,6)" + " x " + opc;

		if (ntipoprevio.equals("1")) {
			list = this.getJdbcTemplate().query(strsql, new FuncionFacturacionMapper());
		} else if (ntipoprevio.equals("2")) {
		} else if (ntipoprevio.equals("3")) {
			list = this.getJdbcTemplate().query(strsql, new CalculoMapper());
		}
		log.info("SQL Execute: " + strsql);
		log.info("Registros obtenidos " + list.size());
		getDatosAdicionales(pCconvenio, pUconsecutivo, list);

		return list;
	}

	@Override
	public List<FuncionFacturacionDto> getDatosAdicionales(Long pCconvenio, String pUconsecutivo,
			List<FuncionFacturacionDto> lstCalculos) {
		List<DatosAdicionalesDto> list = null;
		String var1, var2, var3, strsql;

		if (pCconvenio != 309 && pCconvenio != 310 && pCconvenio != 311 && pCconvenio != 312) {
			var1 = "cconvenio = " + (pCconvenio);
			var3 = "kordensucursal";
		} else {
			var1 = "cconvenio in (309,310,311,312) ";
			var3 = "kpaciente";

		}

		for (FuncionFacturacionDto calculoDto : lstCalculos) {
			if (var3.equals("kordensucursal")) {
				var2 = " select distinct lower(trim (translate (sdatoadicional, '. ', '_'))) as nombrecampo, cda.cdatoadicional, tda.kordensucursal as llave, translate (tda.svalor, '', ' ') svalor, cconvenio "
						+ " from t_dato_adicional tda,  c_dato_adicional cda where tda.cdatoadicional = cda.cdatoadicional and cda.cconvenio = "
						+ (pCconvenio) + " and tda.kordensucursal in ( " + calculoDto.getKordensucursal() + " )";

				list = this.getJdbcTemplate().query(var2, new DatosAdicionalesMapper());
				int j = 1;
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
				var2 = " select distinct lower(trim (translate (sdatoadicional, '. ', '_'))) as nombrecampo, cda.cdatoadicional, tda.kpaciente as llave, translate (tda.svalor, '', ' ') svalor, cconvenio "
						+ " from t_dato_adicional tda,  c_dato_adicional cda  where tda.cdatoadicional = cda.cdatoadicional and cda.cconvenio in (309,310,311,312) and tda.kpaciente in ( "
						+ calculoDto.getKpaciente() + " ) ";
				list = this.getJdbcTemplate().query(var2, new DatosAdicionalesMapper());
				int j = 1;
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
					+ calculoDto.getCexamen() + ")";
			List<ClasificacionDto> lstClasificacion = null;
			lstClasificacion = this.getJdbcTemplate().query(strsql, new ClasificacionMapper());
			if (lstClasificacion.size() > 0)
				calculoDto.setSclasificacioncomercial(lstClasificacion.get(0).getSclasificacioncliente());
		}

		return lstCalculos;
	}

	@Override
	public void actualizaciones(Long pCconvenio, String pUconsecutivo, Boolean pBdefinitivo, Integer pUserid,
			Integer pMaxamount, Integer p_razon, List<FuncionFacturacionDto> lstCalculos) {
		String strsql;

		strsql = "select ec.cmarca from e_convenio ec where cconvenio=" + pCconvenio;
		Integer cmarcasucursal = this.getJdbcTemplate().queryForObject(strsql, Integer.class);
		Integer csucursalmarca = 0;
		String sseriemarca = "";
		Integer centidadlegalmarca = 0;
		String ssucursalmarca = "";

		if (cmarcasucursal == 1) {
			csucursalmarca = 1003;
			sseriemarca = "A";
			centidadlegalmarca = 1;
			ssucursalmarca = "EMPRESAS";
		} else if (cmarcasucursal == 4) {
			csucursalmarca = 1012;
			sseriemarca = "AZ";
			centidadlegalmarca = 5;
			ssucursalmarca = "EMPRESAS AZTECA";
		} else if (cmarcasucursal == 5) {
			csucursalmarca = 1013;
			sseriemarca = "AS";
			centidadlegalmarca = 6;
			ssucursalmarca = "EMPRESAS SWISSLAB";
		} else if (cmarcasucursal == 15) {
			csucursalmarca = 1017;
			sseriemarca = "ASL";
			centidadlegalmarca = 16;
			ssucursalmarca = "EMPRESAS LIACSA";
		} else if (cmarcasucursal == 19) {
			csucursalmarca = 1020;
			sseriemarca = "AFN";
			centidadlegalmarca = 19;
			ssucursalmarca = "EMPRESAS FAMILYLABS NORTE";
		} else if (cmarcasucursal == 20) {
			csucursalmarca = 1021;
			sseriemarca = "AJK";
			centidadlegalmarca = 20;
			ssucursalmarca = "EMPRESAS EXAKTA";
		} else if (cmarcasucursal == 16) {
			csucursalmarca = 1024;
			sseriemarca = "AIO";
			centidadlegalmarca = 18;
			ssucursalmarca = "EMPRESAS MOREIRA";
		} else if (cmarcasucursal == 21) {
			csucursalmarca = 1022;
			sseriemarca = "AAS";
			centidadlegalmarca = 21;
			ssucursalmarca = "EMPRESAS ASESORES DEL SUR";
		} else if (cmarcasucursal == 22) {
			csucursalmarca = 1023;
			sseriemarca = "AIM";
			centidadlegalmarca = 22;
			ssucursalmarca = "EMPRESAS POLAB";
		} else if (cmarcasucursal == 23) {
			csucursalmarca = 1027;
			sseriemarca = "AIP";
			centidadlegalmarca = 23;
			ssucursalmarca = "EMPRESAS LISTER";
		} else if (cmarcasucursal == 25) {
			csucursalmarca = 1024;
			sseriemarca = "AMZ";
			centidadlegalmarca = 25;
			ssucursalmarca = "EMPRESAS BR";
		} else if (cmarcasucursal == 26) {
			csucursalmarca = 1025;
			sseriemarca = "AIN";
			centidadlegalmarca = 26;
			ssucursalmarca = "EMPRESAS PROMEDIC";
		} else if (cmarcasucursal == 9) {
			csucursalmarca = 1028;
			sseriemarca = "AIQ";
			centidadlegalmarca = 10;
			ssucursalmarca = "EMPRESAS SWISSHOSPITAL";
		} else if (cmarcasucursal == 7) {
			if (p_razon == 1) {
				csucursalmarca = 1014;
				sseriemarca = "AJP";
				centidadlegalmarca = 7;
				ssucursalmarca = "EMPRESAS JENNER PRADO";
			} else if (p_razon == 2) {
				csucursalmarca = 1015;
				sseriemarca = "AJL";
				centidadlegalmarca = 8;
				ssucursalmarca = "EMPRESAS JENNER LEAN";
			} else if (p_razon == 3) {
				csucursalmarca = 1012;
				sseriemarca = "AZ";
				centidadlegalmarca = 5;
				ssucursalmarca = "EMPRESAS AZTECA";
			}
		}

		if (pBdefinitivo = true && pMaxamount == -1 && pCconvenio != 309 && pCconvenio != 310 && pCconvenio != 311
				&& pCconvenio != 312) {
			log.info("entre global");
			strsql = "select ufolioactual from c_control_folio where sserie = '" + sseriemarca + "' AND CSUCURSAL = "
					+ csucursalmarca + " AND CESTADOREGISTRO=31";
			Long vFolioActual = this.getJdbcTemplate().queryForObject(strsql, Long.class);

			vFolioActual += 1;
			BigDecimal subtotal = new BigDecimal(0);
			BigDecimal iva = new BigDecimal(0);
			BigDecimal total = new BigDecimal(0);
			List<String> listFacturaSucursal = new ArrayList<>();
			for (FuncionFacturacionDto dto : lstCalculos) {
				subtotal = subtotal.add(dto.getMsubtotal());
				iva = iva.add(dto.getMiva());
				total = total.add(dto.getMtotal());
				listFacturaSucursal.add(String.valueOf(dto.getKordensucursalfac()));

			}
			listFacturaSucursal = listFacturaSucursal.stream().distinct().collect(Collectors.toList());

			String inKordensucursaclfac = String.join(",", listFacturaSucursal);

			strsql = "insert into t_factura values(t_factura_sequence.nextval,(select kdatofiscal from c_convenio_dato_fiscal where cconvenio = "
					+ pCconvenio + ")" + ",'" + ssucursalmarca + "'," + vFolioActual
					+ ",(select ccliente from c_convenio  where cconvenio =" + pCconvenio + ")," + csucursalmarca
					+ ",1," + subtotal + ",0.00,0.00," + " " + iva + "," + "" + total + "" + ",1, " + pCconvenio
					+ ", ' ', '  ', " + centidadlegalmarca + ",sysdate,33,sysdate," + pUserid + "," + pUserid
					+ ", ' ' ,' ' ,  ' ', '" + sseriemarca + "');";

			// this.getJdbcTemplate().execute(strsql);

			strsql = "update c_control_folio set ufolioactual = " + vFolioActual + " where csucursal = "
					+ csucursalmarca + " and cestadoregistro=31 and sserie='" + sseriemarca + "'; ";
			// this.getJdbcTemplate().execute(strsql);

			strsql = "update t_orden_sucursal_fac set kfactura = (select kfactura from t_factura where sserie='"
					+ sseriemarca + "' and csucursal = " + csucursalmarca + " and ufoliofactura = " + vFolioActual + ")"
					+ ", cestadoregistro= 35 where cconvenio = " + pCconvenio
					+ " and cestadoregistro = 37 and kordensucursalfac in (" + inKordensucursaclfac + ");";
			// this.getJdbcTemplate().execute(strsql);

			strsql = "update t_orden_examen_sucursal_fac set kfactura = (select kfactura from t_factura where sserie='"
					+ sseriemarca + "' and csucursal = " + csucursalmarca + " and ufoliofactura = " + vFolioActual
					+ ") " + "where cestadoregistro <> 43 and kordensucursalfac in (" + inKordensucursaclfac + ");";
			// this.getJdbcTemplate().execute(strsql);

		}

		if (pBdefinitivo = true && pMaxamount > -1 && pCconvenio != 309 && pCconvenio != 310 && pCconvenio != 311
				&& pCconvenio != 312) {
			Double macumulador = 0.0;
			BigDecimal subtotal = new BigDecimal(0);
			BigDecimal iva = new BigDecimal(0);
			BigDecimal total = new BigDecimal(0);
			List<String> listFacturaSucursal = new ArrayList<>();
			for (FuncionFacturacionDto dto : lstCalculos) {

				subtotal = subtotal.add(dto.getMsubtotal());
				iva = iva.add(dto.getMiva());
				total = total.add(dto.getMtotal());
				listFacturaSucursal.add(String.valueOf(dto.getKordensucursalfac()));
				macumulador += total.doubleValue();
				if (macumulador < pMaxamount)
					continue;
				else
					break;

			}
			listFacturaSucursal = listFacturaSucursal.stream().distinct().collect(Collectors.toList());

			String inKordensucursaclfac = String.join(",", listFacturaSucursal);

			strsql = "select ufolioactual  from c_control_folio where sserie = " + sseriemarca + " AND CSUCURSAL = "
					+ csucursalmarca + " AND CESTADOREGISTRO=31";
			Long vFolioActual = this.getJdbcTemplate().queryForObject(strsql, Long.class);

			vFolioActual += 1;
			strsql = "insert into t_factura values(t_factura_sequence.nextval,(select kdatofiscal from c_convenio_dato_fiscal where cconvenio = '"
					+ pCconvenio + "')'||'" + ",'" + ssucursalmarca + "'," + vFolioActual
					+ ",(select ccliente from c_convenio  where cconvenio ='" + pCconvenio + "')'||'," + csucursalmarca
					+ ",1,'''" + "||'||sum (msubtotal)||'''" + "||',0.00,0.00,'''" + "||'||sum (miva)||'','''"
					+ "||'||sum (mtotal)||'''" + "||',1, " + pCconvenio + ", ' ', '  ', '" + centidadlegalmarca
					+ "',sysdate,33,sysdate," + pUserid + "," + pUserid + ", ' ' ,' ' ,  ' ', '" + sseriemarca + "');";
			this.getJdbcTemplate().execute(strsql);

			strsql = "update c_control_folio set ufolioactual = " + vFolioActual + " where csucursal = "
					+ csucursalmarca + " and cestadoregistro=31 and sserie='" + sseriemarca + "';";

			strsql = "update t_orden_sucursal_fac set kfactura = (select kfactura from t_factura where sserie='"
					+ sseriemarca + "' and csucursal = " + csucursalmarca + " and ufoliofactura = " + vFolioActual + ")"
					+ ", cestadoregistro= 35 where cconvenio = '" + pCconvenio
					+ "'and cestadoregistro = 37 and kordensucursalfac in (" + inKordensucursaclfac + ");";
			this.getJdbcTemplate().execute(strsql);

			strsql = "update t_orden_examen_sucursal_fac set kfactura = (select kfactura from t_factura where sserie='"
					+ sseriemarca + "' and csucursal = " + csucursalmarca + " and ufoliofactura = " + vFolioActual
					+ ") " + "where cestadoregistro <> 43 and kordensucursalfac in ('" + inKordensucursaclfac + "');";
			this.getJdbcTemplate().execute(strsql);

		}

	}

}
