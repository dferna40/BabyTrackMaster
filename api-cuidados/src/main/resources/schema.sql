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

CREATE TABLE IF NOT EXISTS tipo_panal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

INSERT INTO tipo_panal (nombre, created_at, updated_at) VALUES
('PIPI', NOW(), NOW()),
('CACA', NOW(), NOW()),
('MIXTO', NOW(), NOW());

ALTER TABLE cuidados
    ADD COLUMN duracion VARCHAR(50);

ALTER TABLE cuidados
    ADD CONSTRAINT fk_cuidados_tipo FOREIGN KEY (tipo) REFERENCES tipo_cuidado(id);

ALTER TABLE cuidados
    ADD COLUMN tipo_panal BIGINT;

ALTER TABLE cuidados
    ADD CONSTRAINT fk_cuidados_tipo_panal FOREIGN KEY (tipo_panal) REFERENCES tipo_panal(id);

ALTER TABLE cuidados
    ADD COLUMN cantidad_panal INT;

