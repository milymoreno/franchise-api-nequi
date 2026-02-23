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
