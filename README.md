select facturacion.func_olab_sp_return_detalle_fac(16449,'0,1,0',1,true,-1,1);

select facturacion.func_actualizaciones_marca(19493,'0,1,0',true,1,'1',-1,1);

select ufolioactual from c_control_folio where sserie = 'A' AND CSUCURSAL = 1003 AND CESTADOREGISTRO=31;

select * from t_orden_sucursal_fac tf where cestadoregistro = 37;
