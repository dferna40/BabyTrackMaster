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

CREATE TABLE IF NOT EXISTS alimentacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    bebe_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lado VARCHAR(10),
    duracion_min INT,
    tipo_lactancia_id BIGINT,
    tipo_leche VARCHAR(30),
    cantidad_ml INT,
    cantidad_leche_formula INT,
    alimento VARCHAR(100),
    cantidad VARCHAR(50),
    cantidad_otros_alimentos INT,
    observaciones VARCHAR(255),
    eliminado BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
