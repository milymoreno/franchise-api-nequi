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

## Despliegue en AWS con Terraform

### Requisitos

- [Terraform](https://developer.hashicorp.com/terraform/install) >= 1.5
- AWS CLI configurado (`aws configure`)
- Cuenta en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) con un cluster creado

### 1. Configurar variables

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
# Edita terraform.tfvars con tu MONGODB_URI de Atlas
```

### 2. Construir el jar y la imagen Docker

```bash
./mvnw package -DskipTests
./mvnw spring-boot:build-image -DskipTests
```

### 3. Subir imagen a ECR (opcional) o usar Dockerrun.aws.json

El Elastic Beanstalk usara el `Dockerfile` incluido en el zip de despliegue.

### 4. Inicializar y aplicar Terraform

```bash
cd terraform
terraform init
terraform plan
terraform apply
```

Al finalizar, Terraform imprime la URL publica de la app:

```
Outputs:
app_url = "http://franchise-api-production.us-east-1.elasticbeanstalk.com"
```

### 5. Destruir infraestructura

```bash
terraform destroy
```
