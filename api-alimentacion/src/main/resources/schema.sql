CREATE TABLE IF NOT EXISTS alimentacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    bebe_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    lado VARCHAR(10),
    duracion_min INT,
    tipo_leche VARCHAR(30),
    cantidad_ml INT,
    alimento VARCHAR(100),
    cantidad VARCHAR(50),
    observaciones VARCHAR(255),
    eliminado BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
