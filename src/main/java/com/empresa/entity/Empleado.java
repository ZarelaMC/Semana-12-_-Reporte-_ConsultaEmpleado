package com.empresa.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "empleado")
public class Empleado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idEmpleado;
	private String nombres;
	private String apellidos;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" , timezone = "America/Lima")
	private Date fechaNacimiento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss" , timezone = "America/Lima")
	private Date fechaRegistro;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss" , timezone = "America/Lima")
	private Date fechaActualizacion;
	
	private int estado;
	
	@ManyToOne
	@JoinColumn(name = "idPais")
	private Pais pais;
	
	//Semana 12
	//_____________ CREAR NUEVOS ATRIBUTOS A PARTIR DE OTROS para el Reporte _____________
	//Para obtener el Estado
	public String getReporteEstado() {
		return estado == 1 ? "Activo" : "Inactivo";
	}
	
	//Para obtener el nombre del Paìs
	public String getReportePais() {
		return pais.getNombre();
	}
	
	//Para formatear la fecha de nacimiento 
	public String getReporteFechaNacimiento() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(fechaNacimiento);
	}

	/*//Para formatear la fecha de registro 
	public String getReporteFechaRegistro() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(fechaRegistro);
	}*/
}





