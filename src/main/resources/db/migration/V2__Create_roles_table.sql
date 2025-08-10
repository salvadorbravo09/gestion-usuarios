-- Migración: Crear tabla roles
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Contraint unico para el nombre del rol
ALTER TABLE roles ADD CONSTRAINT uk_roles_name UNIQUE (name);

-- Insertar roles basicos
INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_ADMIN');
