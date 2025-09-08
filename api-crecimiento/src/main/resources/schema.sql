CREATE TABLE IF NOT EXISTS tipo_crecimiento (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS crecimientos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bebe_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    tipo BIGINT NOT NULL,
    fecha TIMESTAMP NOT NULL,
    valor DOUBLE NOT NULL,
    unidad VARCHAR(20),
    observaciones VARCHAR(500),
    eliminado BIT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_crecimientos_tipo FOREIGN KEY (tipo) REFERENCES tipo_crecimiento(id)
);
