# SS-Usuarios-Service

Microservicio de usuarios para el sistema **Sanos y Salvos**.  
Este servicio gestiona usuarios, autenticación con Google OAuth2, generación/validación de JWT y persistencia en PostgreSQL.

## Tecnologías

- Java 21
- Spring Boot
- Spring Security
- OAuth2 Client
- JWT
- Spring Data JPA
- PostgreSQL
- Docker
- Docker Compose
- Maven

## Requisitos

- Java 21
- Maven
- Docker Desktop
- Git

## Variables de entorno

Crear un archivo `.env` en la raíz del proyecto:

```env
DB_USER=postgres
DB_PASSWORD=tu_password_segura
GOOGLE_CLIENT_ID=tu_google_client_id
GOOGLE_CLIENT_SECRET=tu_google_client_secret
JWT_SECRET=tu_jwt_secret_seguro
Ejecución con Docker Compose
docker compose up --build

El servicio quedará disponible en:

http://localhost:8080

La base de datos PostgreSQL quedará disponible en:

localhost:5432
Base de datos

Nombre de la base de datos:

usuarios_db

El proyecto usa PostgreSQL y Spring Data JPA.

Configuración principal

El servicio usa variables de entorno para configurar:

Puerto del servidor
URL de PostgreSQL
Usuario y contraseña de base de datos
Credenciales de Google OAuth2
Secreto JWT
