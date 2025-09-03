CREATE TABLE IF NOT EXISTS tipo_cuidado (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

INSERT INTO tipo_cuidado (nombre, created_at, updated_at) VALUES
('ALIMENTACION', NOW(), NOW()),
('HIGIENE', NOW(), NOW()),
('BANO', NOW(), NOW()),
('SUENO', NOW(), NOW()),
('MEDICACION', NOW(), NOW()),
('PASEO', NOW(), NOW());

ALTER TABLE cuidados
    ADD COLUMN pecho VARCHAR(10);

ALTER TABLE cuidados
    ADD CONSTRAINT fk_cuidados_tipo FOREIGN KEY (tipo) REFERENCES tipo_cuidado(id);

