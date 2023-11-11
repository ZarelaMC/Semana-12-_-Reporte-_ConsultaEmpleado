package com.empresa.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empresa.entity.Empleado;
import com.empresa.service.EmpleadoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.apachecommons.CommonsLog;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
//Para imprimir en consola - herramienta de Lombox
@CommonsLog
public class EmpleadoConsultaController {

	@Autowired
	private EmpleadoService empleadoService;

	@GetMapping("/verConsultaEmpleado")
	public String verInicio() {
		return "consultaEmpleado";
	}
	
	@GetMapping("/consultaEmpleado")
	@ResponseBody
	public List<Empleado> consulta (int estado, 
									int idPais, 
									String nomApe, 
									@DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
									@DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta){
		
		List<Empleado> lstSalida = empleadoService.listaConsultaEmpleado(estado, idPais, "%"+nomApe+"%", desde, hasta);
		
		return lstSalida;
	}
	
	@GetMapping("/reporteEmpleadoPdf")
	//request - variable para enviar paràmetros
	//response - variable para recibir paràmetros
	public void reportes(HttpServletRequest request,
						HttpServletResponse response,
						boolean paramEstado,
						int paramPais, //de la casilla de verificaciòn
						String paramNomApe, 
						@DateTimeFormat(pattern = "yyyy-MM-dd") Date paramDesde,
						@DateTimeFormat(pattern = "yyyy-MM-dd") Date paramHasta){
							
		try  {
		//PASO 1: Obtener el dataSource que va generar el reporte
		List<Empleado> lstSalida = empleadoService.listaConsultaEmpleado(   paramEstado ?1:0, 
																			paramPais, 
																			"%" + paramNomApe + "%", 
																			paramDesde, 
																			paramHasta);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lstSalida);
		
		//PASO 2: Obtener el archivo que contiene el diseño del reporte
			String fileDirectory = request.getServletContext().getRealPath("/WEB-INF/reportes/ReporteBecados.jasper"); 
			log.info(">>> File >> " + fileDirectory);
			
			//enmascarar para convertirlo en bits
			FileInputStream stream = new FileInputStream(new File(fileDirectory));
				
		//PASO 3: Parametros adicionales
			String fileLogo = request.getServletContext().getRealPath("/WEB-INF/img/cherry.jpg");
			log.info(">>> File Logo >> " + fileLogo);
				
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("RUTA_LOGO", fileLogo);
			
		//PASO 4: Enviamos dataSource, diseño y parámetros para generar el PDF
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(stream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
			
		//PASO 5: Enviar el PDF generado
	  		response.setContentType("application/x-pdf");
	  	    response.addHeader("Content-disposition", "attachment; filename=ReporteEmpleado.pdf");

	  		OutputStream outStream = response.getOutputStream();
	  		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	  		
	  		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
	