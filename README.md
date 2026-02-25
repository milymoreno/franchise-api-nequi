# franchise-api

REST API para gestión de franquicias, sucursales y productos.

**Prueba Técnica NEQUI**

---

## Stack

- Java 17
- Spring Boot 3.5 + Spring WebFlux (reactivo)
- Spring Data MongoDB Reactive
- Maven
- Docker

---

## Ejecutar localmente

### Requisitos

- Java 17
- Maven 3.8+
- Docker

### 1. Levantar MongoDB

```bash
docker run -d -p 27017:27017 --name mongo-local mongo:7
```

Verificar que está corriendo:

```bash
docker ps
```

### 2. Correr la app

```bash
./mvnw spring-boot:run
```

La app queda en: `http://localhost:8080`

### 3. Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

## Ejecutar con Docker Compose

Compila primero el jar:

```bash
./mvnw package -DskipTests
```

Levanta todo (app + MongoDB):

```bash
docker compose up --build
```

---

## Variables de entorno

| Variable | Default | Descripcion |
|----------|---------|-------------|
| `MONGODB_URI` | `mongodb://localhost:27017/franchise-db` | URI de conexion a MongoDB |

Para conectar a MongoDB Atlas:

```bash
MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/franchise-db ./mvnw spring-boot:run
```

---

## Endpoints

### Franquicias

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | `/franchises` | Crear franquicia |
| GET | `/franchises/{id}` | Obtener franquicia con sucursales y productos |
| PATCH | `/franchises/{id}/name` | Renombrar franquicia |
| GET | `/franchises/{id}/top-stock-products` | Producto con mas stock por sucursal |

### Sucursales

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | `/franchises/{franchiseId}/branches` | Agregar sucursal |
| PATCH | `/branches/{id}/name` | Renombrar sucursal |

### Productos

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | `/branches/{branchId}/products` | Agregar producto |
| DELETE | `/branches/{branchId}/products/{productId}` | Eliminar producto |
| PATCH | `/products/{id}/stock` | Actualizar stock |
| PATCH | `/products/{id}/name` | Renombrar producto |

---

## Seguridad (JWT)

Los endpoints de lectura (`GET`) y la documentación de Swagger son públicos.
Todos los endpoints de escritura (`POST`, `PATCH`, `DELETE`) están protegidos mediante **Spring Security y JWT**.

Para autenticarse:
1. Llamar al endpoint de login con las credenciales por defecto:
   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "admin123"}'
   ```
2. El API retornará un token JWT.
3. Usar ese token en el header de autorización para los demás endpoints:
   `Authorization: Bearer <tu-token-jwt>`

En **Swagger UI**, utiliza el botón "Authorize" en la parte superior derecha para pegar el token y probar los endpoints protegidos.

---

## Arquitectura

Arquitectura Hexagonal (Ports & Adapters) con principios SOLID:

```
HTTP Request
  → Controller (WebFlux)
    → UseCase (Port In)
      → Domain Service
        → Repository Port (Port Out)
          → MongoDB Adapter
```

El dominio no depende de Spring, MongoDB ni ningún framework.

---

## Despliegue en AWS con Terraform y Elastic Beanstalk

### Requisitos

- [Terraform](https://developer.hashicorp.com/terraform/install) >= 1.5
- AWS CLI configurado (`aws configure`)
- Cuenta en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) con un cluster creado (Permitir IP `0.0.0.0/0` en Network Access)

### 1. Configurar variables de infraestructura

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
# Edita terraform.tfvars con tu MONGODB_URI de Atlas
```

### 2. Crear la infraestructura (S3, IAM, Elastic Beanstalk)

```bash
cd terraform
terraform init
terraform plan
terraform apply -auto-approve
```

Al finalizar, Terraform imprimirá el nombre del entorno y el bucket S3 creado.

### 3. Empaquetar y Desplegar la Aplicación

La infraestructura de Terraform está configurada para empaquetar automáticamente el código y desplegarlo en AWS Elastic Beanstalk cada vez que haya cambios en el JAR o el Dockerfile.

Solo debes compilar el JAR y aplicar Terraform nuevamente:

```bash
# 1. Compilar JAR
./mvnw package -DskipTests

# 2. Desplegar usando Terraform
cd terraform
terraform apply -auto-approve
```

> **Nota:** Terraform tomará automáticamente el archivo `Dockerfile.eb`, lo renombrará a `Dockerfile`, lo empaquetará junto al JAR y actualizará el entorno.

La app estará disponible en la URL generada por Elastic Beanstalk (ej. `http://franchise-api-production...elasticbeanstalk.com/swagger-ui.html`).

### Destruir infraestructura

```bash
terraform destroy
```
