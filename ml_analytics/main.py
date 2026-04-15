from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

# Definimos la estructura exacta que Spring Boot nos va a enviar
class DatosPaciente(BaseModel):
    peso_kg: float
    talla_cm: int
    presion_sistolica: int
    presion_diastolica: int
    fumador: bool
    antecedentes_diabetes: bool
    estilo_vida: str

@app.post("/api/predict/salud")
def predecir_riesgo(datos: DatosPaciente):
    print(f"Datos recibidos desde Java: {datos}")
    
    # Lógica de Mock dinámico basado en los datos reales
    riesgo = "Saludable"
    probabilidad = 0.15
    
    if datos.presion_sistolica > 130 or datos.presion_diastolica > 85:
        riesgo = "Riesgo Hipertensión"
        probabilidad = 0.75
        
    if datos.antecedentes_diabetes and datos.peso_kg > 90:
        riesgo = "Riesgo Alto (Diabetes)"
        probabilidad = 0.88
        
    if datos.fumador:
        probabilidad += 0.10

    # Retornamos el JSON a Spring Boot
    return {
        "prediccion": riesgo,
        "probabilidad": min(probabilidad, 0.99)
    }