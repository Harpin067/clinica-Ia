package com.example.clinica.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RegistroExpedienteDTO {
    // Datos del Paciente
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String telefono; // Opcional por ahora
    
    // Features Médicos para ML
    private BigDecimal peso;
    private Integer talla;
    private Integer presionSistolica;
    private Integer presionDiastolica;
    private Boolean fumador;
    private Boolean antecedentesDiabetes;
    private String actividadFisica;
}