# Contexto del Proyecto - Microservicios Restaurante / Plazoleta de Comidas

Este documento es la fuente de verdad versionada en Git sobre qué se va a hacer, cómo se organiza el trabajo y la arquitectura del sistema.

## 1. Estructura de mensajes de commit

Formato sugerido: `tipo(modulo): descripción corta [HU-xx]`

Tipos:
- `feat`: nueva funcionalidad
- `fix`: corrección de error
- `docs`: documentación
- `test`: pruebas
- `refactor`: mejora interna sin cambiar comportamiento
- `chore`: configuración o tareas de soporte

Ejemplo: `feat(plazoleta): crear endpoint crear restaurante [HU-02]`

## 2. Estrategia Git Flow

Ramas principales:
- `main`: versión estable y entregable.
- `develop`: rama de integración del trabajo del sprint.

Ramas de apoyo:
- `feature/HU-01-crear-propietario`
- `feature/HU-02-crear-restaurante`
- `feature/HU-03-crear-plato`
- `feature/HU-11-realizar-pedido`
- `release/sprint-1`
- `release/sprint-2`
- `release/sprint-3`
- `hotfix/correccion-login` (si surge una corrección urgente)

Flujo recomendado:
1. Desde `develop`, cada integrante crea su rama `feature/...`.
2. Trabaja su historia de usuario y hace commits frecuentes.
3. Abre un Pull Request a `develop`.
4. El equipo revisa y aprueba.
5. Al cierre del sprint, se crea una rama `release/sprint-x`.
6. Tras validar pruebas y documentación, se fusiona a `main`.
7. Si aparece un error crítico en la entrega, se corrige en `hotfix/...`.

Reglas prácticas:
- Cada integrante debe tener mínimo 3 commits por semana en días distintos.
- Cada HU debe salir de una rama `feature`, ningún cambio va directo a `main` y toda fusión a `develop` debe quedar asociada a una HU.
- La documentación también debe quedar versionada en Git.

## 3. Arquitectura basada en microservicios

> Objetivo: **escalabilidad + independencia + mantenibilidad** con despliegue distribuido y bajo acoplamiento (REST).

### Vista rápida — 4 servicios

| # | Microservicio | Rol principal | ¿Qué resuelve? |
|---|---------------|---------------|----------------|
| 1 | **Usuarios** | Auth & Roles | Login, bcrypt, permisos |
| 2 | **Plazoleta** | Núcleo del negocio | Restaurantes, platos, pedidos |
| 3 | **Trazabilidad** | Ciclo de vida del pedido | Estados + métricas |
| 4 | **Notificaciones** | Comunicación | SMS / PIN pedido listo |

```
[ Cliente ] → Usuarios (auth) → Plazoleta (pedido) → Trazabilidad (estados) → Notificaciones (SMS)
```

### 3.1. Microservicio de Usuarios — *Identidad y acceso*

> Centraliza auth para que todo lo demás esté protegido.

| Responsabilidad | Detalle |
|-----------------|---------|
| **Gestión de usuarios** | Administrador, Propietario, Empleado, Cliente |
| **Autenticación** | Login con correo + contraseña |
| **Seguridad** | Bcrypt + validación de roles + autorización por endpoint |

**HU asociadas:** Crear propietario · Agregar autenticación · Crear cuenta empleado · Crear cuenta cliente

### 3.2. Microservicio de Plazoleta — *Corazón del negocio*

> Eje central entre clientes, propietarios y empleados.

| Dominio | Responsabilidades |
|---------|-------------------|
| **Restaurantes** | Creación y asociación con propietario |
| **Platos** | Crear, modificar, habilitar/deshabilitar |
| **Consultas** | Listar restaurantes (paginado) · Listar platos por restaurante (filtros) |
| **Pedidos** | Creación + reglas: un pedido activo por cliente, todo del mismo restaurante |

**HU asociadas:** Crear restaurante · Crear/modificar/habilitar plato · Listar restaurantes/platos · Realizar pedido · Consultar pedidos por estado

### 3.3. Microservicio de Trazabilidad — *Estados y auditoría*

> Desacopla el seguimiento para auditar y medir sin tocar el pedido.

| Responsabilidad | Detalle |
|-----------------|---------|
| **Estados** | `Pendiente` → `En preparación` → `Listo` → `Entregado` |
| **Asignación** | Pedido → Empleado |
| **Historial** | Log de cada cambio de estado + consulta cliente |
| **Métricas** | Tiempo de atención por pedido · Ranking eficiencia por empleado |

**HU asociadas:** Asignarse a pedido · Cambiar a "en preparación" · Marcar entregado · Cancelar · Consultar trazabilidad/eficiencia

### 3.4. Microservicio de Notificaciones — *Comunicación*

> Desacopla el envío para escalar o cambiar proveedor sin afectar el resto.

| Responsabilidad | Detalle |
|-----------------|---------|
| **Notificar** | Aviso al cliente (SMS u otro canal) |
| **Seguridad entrega** | Generación y envío de PIN |
| **Integración** | Servicio externo de mensajería (real/simulado) · Evento `pedido listo` |

**HU asociada:** Notificar que el pedido está listo

## 4. Relación entre microservicios

Interacción mediante APIs REST (o mensajería, si el diseño lo contempla), bajo acoplamiento.

Flujo simplificado:
1. Usuarios gestiona autenticación y roles.
2. Plazoleta gestiona restaurantes, platos y pedidos.
3. Trazabilidad gestiona los estados y el historial de los pedidos.
4. Notificaciones comunica al cliente cuando el pedido está listo.
