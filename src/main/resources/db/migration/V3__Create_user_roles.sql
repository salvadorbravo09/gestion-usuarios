-- Migración: Crear tabla intermedia user_roles
CREATE TABLE user_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Claves foráneas
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_role
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;

-- Constaint unico para evitar duplicados
ALTER TABLE user_roles ADD CONSTRAINT uk_user_roles_user_role UNIQUE (user_id, role_id);