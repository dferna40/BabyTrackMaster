CREATE TABLE IF NOT EXISTS tipo_lactancia (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipo_lactancia (id, nombre) VALUES
    (1,'complementaria'),
    (2,'diferida'),
    (3,'directa'),
    (4,'tandem'),
    (5,'exclusiva'),
    (6,'mixta'),
    (7,'predominante');

CREATE TABLE IF NOT EXISTS tipo_alimentacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipo_alimentacion (id, nombre) VALUES
    (1,'Lactancia'),
    (2,'Biberón'),
    (3,'Sólidos');

CREATE TABLE IF NOT EXISTS tipo_leche_biberon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipo_leche_biberon (id, nombre) VALUES
    (1,'Materna'),
    (2,'Fórmula'),
    (3,'Mixta');

CREATE TABLE IF NOT EXISTS tipo_alimentacion_solidos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipo_alimentacion_solidos (id, nombre) VALUES
    (1,'Frutas'),
    (2,'Verduras'),
    (3,'Cereales'),
    (4,'Otros');

CREATE TABLE IF NOT EXISTS alimentacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    bebe_id BIGINT NOT NULL,
    tipo_alimentacion_id BIGINT NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lado VARCHAR(10),
    duracion_min INT,
    tipo_lactancia_id BIGINT,
    tipo_biberon_id BIGINT,
    cantidad_ml INT,
    cantidad_leche_formula INT,
    tipo_alimentacion_solido_id BIGINT,
    cantidad VARCHAR(50),
    cantidad_otros_alimentos INT,
    alimentacion_otros VARCHAR(100),
    observaciones VARCHAR(255),
    eliminado BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
