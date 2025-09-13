CREATE TABLE IF NOT EXISTS tipo_lactancia (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tipo_alergia (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tipo_grupo_sanguineo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS bebes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    sexo VARCHAR(10) NOT NULL,
    peso_nacer DOUBLE,
    talla_nacer DOUBLE,
    semanas_gestacion INT,
    perimetro_craneal_nacer DOUBLE,
    peso_actual DOUBLE,
    talla_actual DOUBLE,
    bebe_activo BOOLEAN,
    numero_ss VARCHAR(50),
    tipo_lactancia_id BIGINT,
    tipo_alergia_id BIGINT,
    tipo_grupo_sanguineo_id BIGINT,
    medicaciones VARCHAR(500),
    pediatra VARCHAR(100),
    centro_medico VARCHAR(255),
    telefono_centro_medico VARCHAR(20),
    observaciones VARCHAR(1000),
    imagen_bebe LONGBLOB,
    eliminado BOOLEAN NOT NULL,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

