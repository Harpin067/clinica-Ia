package com.example.clinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Integer idConsulta;

    // Relación con el Paciente (Muchos a Uno)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @Column(name = "id_cita")
    private Integer idCita; // Lo dejamos simple por ahora

    @Column(columnDefinition = "TEXT")
    private String motivo;

    // ==========================================
    // FEATURES PARA MACHINE LEARNING (Python)
    // ==========================================
    @Column(name = "peso_kg", precision = 5, scale = 2)
    private BigDecimal pesoKg;

    @Column(name = "talla_cm")
    private Integer tallaCm;

    @Column(name = "presion_sistolica")
    private Integer presionSistolica; // Ej. 120

    @Column(name = "presion_diastolica")
    private Integer presionDiastolica; // Ej. 80

    @Column(name = "paciente_fumador")
    private Boolean pacienteFumador = false;

    @Column(name = "historial_familiar_diabetes")
    private Boolean historialFamiliarDiabetes = false;

    @Column(name = "estilo_vida", length = 50)
    private String estiloVida; // 'sedentario', 'moderado', 'activo'
    // ==========================================

    @Column(precision = 4, scale = 1)
    private BigDecimal temperatura;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "diagnostico_ml", length = 100)
    private String diagnosticoMl;

    @CreationTimestamp
    @Column(name = "fecha_consulta", updatable = false)
    private LocalDateTime fechaConsulta;
}