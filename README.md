# Franchise API

REST API reactiva para gestión de franquicias, sucursales y productos.
Desarrollada como prueba técnica para Accenture.

## Tabla de Contenidos

- [Arquitectura](#arquitectura)
- [Stack Tecnológico](#stack-tecnológico)
- [Requisitos Previos](#requisitos-previos)
- [Ejecución Local](#ejecución-local)
- [Ejecución con Docker](#ejecución-con-docker)
- [Infraestructura con Terraform](#infraestructura-con-terraform)
- [Endpoints](#endpoints)
- [Pruebas](#pruebas)
- [Decisiones Técnicas](#decisiones-técnicas)

---

## Arquitectura

El proyecto sigue **Clean Architecture**, separando responsabilidades en capas independientes:

```text
src/main/java/com/accenture/franchise/
├── domain/                         # Núcleo — sin dependencias externas
│   ├── model/                      # Entidades: Franchise, Branch, Product
│   └── port/
│       ├── in/                     # Contratos de entrada (casos de uso)
│       └── out/                    # Contratos de salida (repositorios)
├── application/
│   └── usecase/                    # Implementación de la lógica de negocio
└── infrastructure/
    ├── adapter/
    │   ├── in/web/                 # Handlers y Router (WebFlux funcional)
    │   └── out/mongo/              # Adaptador MongoDB + Mapper
    └── config/                     # Configuración de Spring y manejo de errores
```

```text
┌─────────────────────────────────────────┐
│              HTTP Request               │
└────────────────────┬────────────────────┘
                     │
         ┌───────────▼───────────┐
         │     FranchiseRouter   │  RouterFunction (funcional)
         └───────────┬───────────┘
                     │
         ┌───────────▼───────────┐
         │   *Handler classes    │  Traduce HTTP ↔ dominio
         └───────────┬───────────┘
                     │
         ┌───────────▼───────────┐
         │    *UseCase (port)    │  Contratos del dominio
         └───────────┬───────────┘
                     │
         ┌───────────▼───────────┐
         │  *UseCaseImpl (app)   │  Lógica de negocio reactiva
         └───────────┬───────────┘
                     │
         ┌───────────▼───────────┐
         │  FranchiseRepository  │  Puerto de salida
         └───────────┬───────────┘
                     │
         ┌───────────▼───────────┐
         │ RepositoryAdapter     │  Implementación MongoDB
         └───────────┬───────────┘
                     │
         ┌───────────▼───────────┐
         │       MongoDB         │
         └───────────────────────┘
```

---

## Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| Framework | Spring Boot 3.4.5 + WebFlux |
| Programación reactiva | Project Reactor (Mono / Flux) |
| Base de datos | MongoDB (local o Atlas) |
| IaC | Terraform + MongoDB Atlas Provider |
| Contenedor | Docker + Docker Compose |
| Tests | JUnit 5 + Mockito + StepVerifier |
| Build | Maven 3.9 |
| Java | 21 |

---

## Requisitos Previos

### Ejecución local sin Docker
- Java 21
- Maven 3.9+
- MongoDB corriendo en `localhost:27017`

### Ejecución con Docker
- Docker 24+
- Docker Compose v2

### Terraform (opcional)
- Terraform 1.6+
- Cuenta en [MongoDB Atlas](https://www.mongodb.com/cloud/atlas/register)

---

## Ejecución Local

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd franchise-api
```

### 2. Levantar MongoDB con Docker

```bash
docker compose up mongodb -d
```

### 3. Correr la aplicación

```bash
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

Verificar que está corriendo:

```bash
curl http://localhost:8080/actuator/health
```

---

## Ejecución con Docker

Levanta la aplicación completa (app + MongoDB) con un solo comando:

```bash
docker compose up --build
```

Para correr en segundo plano:

```bash
docker compose up --build -d
```

Verificar el estado de los contenedores:

```bash
docker compose ps
```

Detener todo:

```bash
docker compose down
```

Detener y eliminar volúmenes (borra los datos):

```bash
docker compose down -v
```

### Usar MongoDB Atlas en lugar de MongoDB local

Edita el `docker-compose.yml` y cambia la variable de entorno de la app:

```yaml
environment:
  MONGODB_URI: mongodb+srv://<user>:<password>@<cluster>.mongodb.net/franchisedb?retryWrites=true&w=majority
  SERVER_PORT: 8080
```

Al usar Atlas puedes eliminar el servicio `mongodb` y el volumen `mongo-data` del `docker-compose.yml` ya que la base de datos corre en la nube.

---

## Infraestructura con Terraform

Provisiona un cluster de MongoDB Atlas (free tier M0) automáticamente.

### 1. Configurar credenciales

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
```

Editar `terraform.tfvars` con tus credenciales de MongoDB Atlas:

```hcl
mongodb_atlas_public_key  = "tu-public-key"
mongodb_atlas_private_key = "tu-private-key"
atlas_org_id              = "tu-org-id"
db_password               = "password-seguro"
```

Para obtener las API Keys: Atlas -> Organization -> **Applications** -> **API Keys** -> Create API Key.
Agregar descripción y asignar el permiso **Organization Project Creator**.

### 2. Inicializar y aplicar

```bash
terraform init
terraform plan
terraform apply
```

### 3. Obtener el connection string

El provider devuelve solo el host. Construye el string completo así:

```bash
terraform output -raw connection_string_srv
# Devuelve: mongodb+srv://.mongodb.net

# String completo para usar en la app:
# mongodb+srv://:@.mongodb.net/franchisedb?retryWrites=true&w=majority
```

### 4. Destruir la infraestructura

```bash
terraform destroy
```

---

## Endpoints

Base URL: `http://localhost:8080`

### Franquicias
| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/franchises` | Crear franquicia |
| `PATCH` | `/api/franchises/{franchiseId}/name` | Actualizar nombre |

### Sucursales
| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/franchises/{franchiseId}/branches` | Agregar sucursal |
| `PATCH` | `/api/franchises/{franchiseId}/branches/{branchId}/name` | Actualizar nombre |

### Productos
| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/franchises/{franchiseId}/branches/{branchId}/products` | Agregar producto |
| `DELETE` | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}` | Eliminar producto |
| `PATCH` | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock` | Modificar stock |
| `PATCH` | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name` | Actualizar nombre |

### Consultas
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/franchises/{franchiseId}/top-products` | Producto con más stock por sucursal |

> La colección completa de Postman con ejemplos de request/response está disponible en [`docs/franchise-api.postman_collection.json`](docs/franchise-api.postman_collection.json).
>
> Importar en Postman: File -> Import -> seleccionar el archivo.

---

## Pruebas

Correr todos los tests:

```bash
mvn test
```

Correr con reporte de cobertura:

```bash
mvn verify
```

Los tests cubren:
- Casos de uso con `StepVerifier` (flujos reactivos)
- Mapper de dominio <-> documento
- Escenarios de error (entidad no encontrada, producto no encontrado)

---

## Decisiones Técnicas

**Programación reactiva con WebFlux**
Se eligió Spring WebFlux sobre Spring MVC para aprovechar el modelo non-blocking end-to-end junto con `ReactiveMongoRepository`, maximizando el throughput sin bloquear threads.

**Modelo de datos embebido en MongoDB**
Las sucursales y productos se almacenan embebidos dentro del documento de franquicia. Esto simplifica las consultas y es apropiado dado que sucursales y productos no tienen identidad independiente fuera de su franquicia.

**Clean Architecture**
La separación en capas domain -> application -> infrastructure garantiza que la lógica de negocio no depende de frameworks. Los puertos (`port/in`, `port/out`) actúan como contratos que permiten cambiar la base de datos o el protocolo HTTP sin tocar los casos de uso.

**Terraform con MongoDB Atlas M0**
Se eligió MongoDB Atlas por su tier gratuito y su provider oficial de Terraform, lo que permite provisionar la base de datos de forma reproducible sin gestionar infraestructura de servidores.

**Docker multi-stage con ZGC**
El build multi-stage reduce el tamaño de la imagen final. Las flags `-XX:+UseZGC` y `-XX:MaxRAMPercentage=75.0` optimizan el garbage collector para contenedores con memoria limitada.