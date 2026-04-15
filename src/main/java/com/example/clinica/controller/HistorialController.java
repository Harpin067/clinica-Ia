package com.example.clinica.controller;

import com.example.clinica.model.Consulta;
import com.example.clinica.model.Paciente;
import com.example.clinica.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historial")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HistorialController {

    private final ConsultaRepository consultaRepository;

    @GetMapping("/pacientes")
    public ResponseEntity<List<Map<String, Object>>> obtenerHistorialCompleto() {
        List<Consulta> todasLasConsultas = consultaRepository.findAll();
        List<Map<String, Object>> historial = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        for (Consulta c : todasLasConsultas) {
            Paciente p = c.getPaciente();
            Map<String, Object> fila = new HashMap<>();
            
            // Datos del Paciente
            fila.put("idConsulta", "CON-" + String.format("%04d", c.getIdConsulta()));
            fila.put("fechaRegistro", c.getFechaConsulta().format(formatter));
            fila.put("nombreCompleto", p.getNombre() + " " + p.getApellido());
            
            // Cálculo de edad
            int edad = Period.between(p.getFechaNacimiento(), LocalDate.now()).getYears();
            String sexoFmt = p.getSexo().equalsIgnoreCase("M") ? "Masculino" : "Femenino";
            fila.put("detallesPaciente", sexoFmt + " • " + edad + " años");
            
            // Signos Vitales
            fila.put("imc", calcularIMC(c));
            fila.put("presion", c.getPresionSistolica() + "/" + c.getPresionDiastolica());
            
            // ML
            fila.put("diagnostico", c.getDiagnosticoMl() != null ? c.getDiagnosticoMl() : "Pendiente");

            historial.add(fila);
        }

        return ResponseEntity.ok(historial);
    }

    private String calcularIMC(Consulta c) {
        if (c.getPesoKg() == null || c.getTallaCm() == null) return "N/A";
        double tallaMts = c.getTallaCm() / 100.0;
        double imc = c.getPesoKg().doubleValue() / (tallaMts * tallaMts);
        return String.format("%.1f", imc);
    }
}