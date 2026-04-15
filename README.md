                                                                        
  ---                                                                           
                                                                                
  # Sistema de Clínica con ML                                                   
                                                                                
  Sistema de gestión clínica construido con Spring Boot, PostgreSQL y un        
  microservicio de analítica en Python/FastAPI para predicción de riesgo de     
  salud.                                                                        
                                                                                
  ## Tecnologías                                                                

  - **Backend:** Java 17, Spring Boot 3.2.2, Spring Data JPA, Lombok            
  - **Base de datos:** PostgreSQL 15
  - **ML Service:** Python, FastAPI                                             
  - **Infraestructura:** Docker, Docker Compose
                                                                                
                                                                                
  El sistema consta de tres servicios:
                                                                                
  | Servicio | Descripción | Puerto |        
  |---|---|---|                                                                 
  | `backend` | API REST Spring Boot | `8080` |
  | `db` | PostgreSQL | `5433` |                                                
  | `ml-service` | Microservicio de predicción FastAPI | interno |
                                                                  
  ## Requisitos                                                                 
               
  - Docker y Docker Compose                                                     
                                             
  ## Levantar el proyecto
                                                                                
  ```bash
  docker compose up --build                                                     
                                             
  La API queda disponible en http://localhost:8080.
                                                                                
  Endpoints principales
                                                                                
  ┌──────────┬─────────────┬────────────────────────┐
  │  Método  │    Ruta     │      Descripción       │
  ├──────────┼─────────────┼────────────────────────┤
  │ GET/POST │ /expediente │ Gestión de expedientes │
  ├──────────┼─────────────┼────────────────────────┤
  │ GET      │ /historial  │ Historial de consultas │                           
  ├──────────┼─────────────┼────────────────────────┤                           
  │ GET      │ /dashboard  │ Datos para dashboard   │                           
  └──────────┴─────────────┴────────────────────────┘                           
                                             
  Predicción de riesgo (ML)                                                     
   
  El backend se comunica internamente con el servicio ML en /api/predict/salud. 
  Recibe datos del paciente (peso, talla, presión arterial, antecedentes) y
  retorna:                                                                      
                                             
  {
    "prediccion": "Riesgo Hipertensión",
    "probabilidad": 0.75
  }                                                                             
   
  Modelos principales                                                           
                                             
  - Paciente — datos demográficos y clínicos del paciente                       
  - Consulta — registro de cada consulta médica
                                                                                
  Base de datos                                                                 
   
  El esquema se inicializa automáticamente desde db_init/init.sql al levantar el
   contenedor de PostgreSQL. Spring Boot aplica migraciones DDL en modo update.
  `
