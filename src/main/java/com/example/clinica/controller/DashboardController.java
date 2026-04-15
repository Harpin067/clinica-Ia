package com.example.clinica.controller;

import com.example.clinica.model.Consulta;
import com.example.clinica.repository.ConsultaRepository;
import com.example.clinica.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> respuesta = new HashMap<>();
        
        // 1. Total de Pacientes Reales
        long totalPacientes = pacienteRepository.count();
        respuesta.put("totalPacientes", totalPacientes);

        // 2. Últimos Pacientes Evaluados (Simularemos traer todos y sacar los últimos para no complicar el Repositorio aún)
        List<Consulta> todasLasConsultas = consultaRepository.findAll();
        List<Map<String, Object>> tablaPacientes = new ArrayList<>();

        for (Consulta c : todasLasConsultas) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("nombre", c.getPaciente().getNombre() + " " + c.getPaciente().getApellido());
            fila.put("signos", "IMC: " + calcularIMC(c) + " • PA: " + c.getPresionSistolica() + "/" + c.getPresionDiastolica());
            fila.put("diagnostico", c.getDiagnosticoMl() != null ? c.getDiagnosticoMl() : "Pendiente");
            tablaPacientes.add(fila);
        }

        // Simulación de porcentajes para el gráfico (En un entorno real se calcularía contando los diagnósticos)
        respuesta.put("grafico", List.of(15, 22, 63)); // Alto, Medio, Bajo
        respuesta.put("ultimosPacientes", tablaPacientes);

        return ResponseEntity.ok(respuesta);
    }

    private String calcularIMC(Consulta c) {
        if (c.getPesoKg() == null || c.getTallaCm() == null) return "N/A";
        double tallaMts = c.getTallaCm() / 100.0;
        double imc = c.getPesoKg().doubleValue() / (tallaMts * tallaMts);
        return String.format("%.1f", imc);
    }
}