-- 1. ESPECIALIDADES
CREATE TABLE IF NOT EXISTS especialidades (
    id_especialidad SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- 2. DOCTORES
CREATE TABLE IF NOT EXISTS doctores (
    id_doctor SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100) UNIQUE,
    id_especialidad INT,
    fecha_ingreso DATE DEFAULT CURRENT_DATE,
    CONSTRAINT fk_doctor_especialidad FOREIGN KEY (id_especialidad) REFERENCES especialidades(id_especialidad)
);

-- 3. PACIENTES
CREATE TABLE IF NOT EXISTS pacientes (
    id_paciente SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    sexo CHAR(1),
    telefono VARCHAR(20),
    direccion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. CITAS MÉDICAS
CREATE TABLE IF NOT EXISTS citas (
    id_cita SERIAL PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_doctor INT NOT NULL,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'Programada',
    CONSTRAINT fk_cita_paciente FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
    CONSTRAINT fk_cita_doctor FOREIGN KEY (id_doctor) REFERENCES doctores(id_doctor)
);

-- 5. CONSULTAS MÉDICAS
CREATE TABLE IF NOT EXISTS consultas (
    id_consulta SERIAL PRIMARY KEY,
    id_cita INT UNIQUE,
    id_paciente INT NOT NULL,
    fecha_consulta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    motivo TEXT,
    peso_kg DECIMAL(5,2),
    talla_cm INT,
    presion_arterial VARCHAR(20),
    temperatura DECIMAL(4,1),
    observaciones TEXT,
    CONSTRAINT fk_consulta_cita FOREIGN KEY (id_cita) REFERENCES citas(id_cita),
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente)
);

-- 6. DIAGNÓSTICOS
CREATE TABLE IF NOT EXISTS diagnosticos (
    id_diagnostico SERIAL PRIMARY KEY,
    id_consulta INT NOT NULL,
    codigo_cie10 VARCHAR(10),
    descripcion TEXT NOT NULL,
    es_cronica BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_diagnostico_consulta FOREIGN KEY (id_consulta) REFERENCES consultas(id_consulta)
);

-- 7. MEDICAMENTOS
CREATE TABLE IF NOT EXISTS medicamentos (
    id_medicamento SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    stock INT NOT NULL CHECK (stock >= 0),
    stock_minimo INT DEFAULT 10,
    precio_venta DECIMAL(10,2) NOT NULL,
    precio_costo DECIMAL(10,2)
);

-- 8. RECETAS MÉDICAS
CREATE TABLE IF NOT EXISTS recetas (
    id_receta SERIAL PRIMARY KEY,
    id_consulta INT NOT NULL,
    fecha_emision DATE DEFAULT CURRENT_DATE,
    CONSTRAINT fk_receta_consulta FOREIGN KEY (id_consulta) REFERENCES consultas(id_consulta)
);

-- 9. DETALLE DE RECETA
CREATE TABLE IF NOT EXISTS receta_detalle (
    id_detalle SERIAL PRIMARY KEY,
    id_receta INT NOT NULL,
    id_medicamento INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    dosis VARCHAR(100),
    frecuencia VARCHAR(100),
    duracion VARCHAR(100),
    CONSTRAINT fk_detalle_receta FOREIGN KEY (id_receta) REFERENCES recetas(id_receta),
    CONSTRAINT fk_detalle_medicamento FOREIGN KEY (id_medicamento) REFERENCES medicamentos(id_medicamento)
);

-- 10. PREDICCIONES DE ML
CREATE TABLE IF NOT EXISTS predicciones_salud (
    id_prediccion SERIAL PRIMARY KEY,
    id_paciente INT NOT NULL,
    tipo_prediccion VARCHAR(50),
    valor_predicho DECIMAL(5,2),
    fecha_ejecucion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prediccion_paciente FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente)
);
