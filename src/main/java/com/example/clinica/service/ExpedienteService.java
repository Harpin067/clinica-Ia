package com.example.clinica.service;

import com.example.clinica.dto.RegistroExpedienteDTO;
import com.example.clinica.model.Consulta;
import com.example.clinica.model.Paciente;
import com.example.clinica.repository.ConsultaRepository;
import com.example.clinica.repository.PacienteRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExpedienteService {

    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;
    private final RestClient restClient;

    // Constructor personalizado para inyectar repositorios e inicializar el cliente HTTP hacia Python
    public ExpedienteService(PacienteRepository pacienteRepository, ConsultaRepository consultaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.consultaRepository = consultaRepository;
        
        // ¡AQUÍ ESTÁ EL CAMBIO CLAVE! 
        // En lugar de localhost, usamos la IP del "puente" de Docker en Linux para salir del contenedor
        this.restClient = RestClient.create("http://172.17.0.1:8000");
    }

    @Transactional
    public Map<String, Object> registrarExpediente(RegistroExpedienteDTO dto) {
        // 1. Separar el nombre completo
        String[] partesNombre = dto.getNombreCompleto().trim().split(" ", 2);
        String nombre = partesNombre[0];
        String apellido = partesNombre.length > 1 ? partesNombre[1] : "";

        // 2. Guardar el Paciente
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSexo(dto.getSexo());
        paciente.setTelefono(dto.getTelefono() != null ? dto.getTelefono() : "Sin registro");
        paciente = pacienteRepository.save(paciente); // Guardamos y obtenemos el ID generado

        // 3. Guardar la Consulta (Features para ML)
        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setPesoKg(dto.getPeso());
        consulta.setTallaCm(dto.getTalla());
        consulta.setPresionSistolica(dto.getPresionSistolica());
        consulta.setPresionDiastolica(dto.getPresionDiastolica());
        consulta.setPacienteFumador(dto.getFumador() != null ? dto.getFumador() : false);
        consulta.setHistorialFamiliarDiabetes(dto.getAntecedentesDiabetes() != null ? dto.getAntecedentesDiabetes() : false);
        consulta.setEstiloVida(dto.getActividadFisica());
        consulta.setMotivo("Triaje inicial / Apertura de expediente");
        
        consulta = consultaRepository.save(consulta);

        // 4. CONEXIÓN REAL CON PYTHON (FastAPI) 🐍
        String prediccionML = "Pendiente";
        try {
            // Preparamos el JSON exacto que espera FastAPI
            Map<String, Object> mlRequest = new HashMap<>();
            mlRequest.put("peso_kg", consulta.getPesoKg());
            mlRequest.put("talla_cm", consulta.getTallaCm());
            mlRequest.put("presion_sistolica", consulta.getPresionSistolica());
            mlRequest.put("presion_diastolica", consulta.getPresionDiastolica());
            mlRequest.put("fumador", consulta.getPacienteFumador());
            mlRequest.put("antecedentes_diabetes", consulta.getHistorialFamiliarDiabetes());
            mlRequest.put("estilo_vida", consulta.getEstiloVida());

            // Hacemos el POST a Python
            Map<String, Object> mlResponse = restClient.post()
                    .uri("/api/predict/salud")
                    .body(mlRequest)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});

            if (mlResponse != null && mlResponse.containsKey("prediccion")) {
                prediccionML = (String) mlResponse.get("prediccion");
            }
        } catch (Exception e) {
            System.err.println("Error conectando a la API de Python: " + e.getMessage());
            prediccionML = "Error de conexión AI";
        }

        // 5. ¡Actualizamos la base de datos con el diagnóstico real!
        consulta.setDiagnosticoMl(prediccionML);
        consultaRepository.save(consulta);

        // 6. Retornar respuesta al Frontend
        return Map.of(
            "mensaje", "Expediente guardado con éxito",
            "idPaciente", paciente.getIdPaciente(),
            "prediccionML", prediccionML
        );
    }
}