# Sistema de Gestión de Usuarios

## Descripción

Sistema de gestión de usuarios desarrollado con Spring Boot 3 que implementa una arquitectura hexagonal (Clean Architecture) con operaciones CRUD completas. El proyecto utiliza MySQL como base de datos y Flyway para las migraciones.

## Arquitectura

El proyecto sigue los principios de la **Arquitectura Hexagonal**, organizando el código en las siguientes capas:

```
src/main/java/com/sbravoc/gestion_usuarios/
├── adapter/          # Capa de adaptadores (REST Controllers, DTOs, Mappers)
├── application/      # Capa de aplicación (Casos de uso, Servicios)
├── domain/           # Capa de dominio (Entidades, Repositorios, Servicios de dominio)
└── infrastructure/   # Capa de infraestructura (Configuraciones específicas)
```

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **MySQL 8.0**
- **Flyway** (Migraciones de base de datos)
- **Lombok** (Reducción de código boilerplate)
- **Docker & Docker Compose**
- **Maven** (Gestión de dependencias)

## Características

- ✅ Operaciones CRUD completas para usuarios
- ✅ Arquitectura hexagonal
- ✅ Migraciones de base de datos con Flyway
- ✅ Dockerización completa
- ✅ Validación de datos
- ✅ Timestamps automáticos (createdAt, updatedAt)
- ✅ Mapeo automático entre DTOs y entidades

## API Endpoints

### Base URL
```
http://localhost:8080/api/v1/users
```

### Operaciones Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET    | `/api/v1/users` | Obtener todos los usuarios |
| GET    | `/api/v1/users/{id}` | Obtener usuario por ID |
| POST   | `/api/v1/users` | Crear nuevo usuario |
| PUT    | `/api/v1/users/{id}` | Actualizar usuario |
| DELETE | `/api/v1/users/{id}` | Eliminar usuario |


## Configuración y Despliegue

### Prerrequisitos

- Java 21 o superior
- Maven 3.6+
- Docker y Docker Compose (para ejecución con contenedores)

### Ejecución con Docker Compose 

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/salvadorbravo09/gestion-usuarios.git
   cd gestion-usuarios
   ```

2. **Ejecutar con Docker Compose**
   ```bash
   docker-compose up --build
   ```

3. **Acceder a la aplicación**
   - API: http://localhost:8080/api/v1/users
   - Base de datos MySQL: localhost:3306

## Migración de Base de Datos

El proyecto utiliza **Flyway** para gestionar las migraciones de base de datos. Los archivos de migración se encuentran en:

```
src/main/resources/db/migration/
└── V1__Create_users_table.sql
```

Las migraciones se ejecutan automáticamente al iniciar la aplicación.

## Contacto

**Desarrollador:** Salvador Bravo  
**Email:** sa.bravo0901@gmail.com