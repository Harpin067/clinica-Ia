package com.example.clinica.controller;

import com.example.clinica.dto.RegistroExpedienteDTO;
import com.example.clinica.service.ExpedienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/expedientes")
@CrossOrigin(origins = "*") // Muy importante para que tu HTML local pueda hacer peticiones sin error de CORS
@RequiredArgsConstructor
public class ExpedienteController {

    private final ExpedienteService expedienteService;

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarNuevoExpediente(@RequestBody RegistroExpedienteDTO dto) {
        try {
            Map<String, Object> respuesta = expedienteService.registrarExpediente(dto);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar: " + e.getMessage()));
        }
    }
}