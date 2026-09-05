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

La solución se desarrollará bajo un enfoque de arquitectura de microservicios, con el objetivo de garantizar escalabilidad, independencia funcional, mantenibilidad y despliegue distribuido.

El sistema estará organizado en los siguientes microservicios principales:

### 3.1. Microservicio de Usuarios

Responsable de la gestión de identidades, autenticación y control de acceso.

Responsabilidades:
- Registro y gestión de usuarios:
  - Administrador
  - Propietario
  - Empleado
  - Cliente
- Autenticación de usuarios (login con correo y contraseña).
- Encriptación de contraseñas mediante bcrypt.
- Validación de roles y permisos para acceso a los servicios.
- Autorización de endpoints según el tipo de usuario.

Historias de usuario asociadas:
- Crear propietario
- Agregar autenticación al sistema
- Crear cuenta empleado
- Crear cuenta cliente

Justificación:
Centraliza la lógica de acceso, asegurando que todas las operaciones estén protegidas mediante autenticación y autorización.

### 3.2. Microservicio de Plazoleta

Núcleo funcional del sistema, encargado de la gestión de restaurantes, menú y pedidos.

Responsabilidades:
- Gestión de restaurantes:
  - Creación de restaurantes
  - Asociación con propietarios
- Gestión de platos:
  - Crear, modificar, habilitar/deshabilitar platos
- Consulta de información:
  - Listado de restaurantes (paginado)
  - Listado de platos por restaurante (con filtros)
- Gestión de pedidos:
  - Creación de pedidos
  - Validación de reglas de negocio (un pedido activo por cliente, pedidos de un mismo restaurante)

Historias de usuario asociadas:
- Crear restaurante
- Crear plato
- Modificar plato
- Habilitar/deshabilitar plato
- Listar restaurantes
- Listar platos de un restaurante
- Realizar pedido
- Consultar pedidos por estado

Justificación:
Concentra la lógica del negocio principal (plazoleta de comidas), eje central entre clientes, propietarios y empleados.

### 3.3. Microservicio de Trazabilidad

Responsable de la gestión del ciclo de vida de los pedidos y su seguimiento histórico.

Responsabilidades:
- Gestión de estados del pedido:
  - Pendiente
  - En preparación
  - Listo
  - Entregado
- Asignación de pedidos a empleados.
- Registro de cada cambio de estado (logs).
- Consulta de trazabilidad por parte del cliente.
- Cálculo de métricas de desempeño:
  - Tiempo de atención por pedido
  - Ranking de eficiencia por empleado

Historias de usuario asociadas:
- Asignarse a un pedido
- Cambiar estado a "en preparación"
- Marcar pedido como entregado
- Cancelar pedido
- Consultar trazabilidad
- Consultar eficiencia de pedidos

Justificación:
Desacopla seguimiento y análisis del pedido, facilitando auditoría, métricas y mejora continua.

### 3.4. Microservicio de Notificaciones

Encargado de la comunicación con el cliente a través de mensajes SMS u otros medios.

Responsabilidades:
- Envío de notificaciones al cliente.
- Generación y envío de PIN de seguridad para la entrega del pedido.
- Integración con servicios externos de mensajería (simulado o real, según alcance).
- Soporte a eventos del sistema (ej: pedido listo).

Historias de usuario asociadas:
- Notificar que el pedido está listo

Justificación:
Desacopla la comunicación del resto del sistema, permitiendo escalar o integrar servicios externos sin afectar otros componentes.

## 4. Relación entre microservicios

Interacción mediante APIs REST (o mensajería, si el diseño lo contempla), bajo acoplamiento.

Flujo simplificado:
1. Usuarios gestiona autenticación y roles.
2. Plazoleta gestiona restaurantes, platos y pedidos.
3. Trazabilidad gestiona los estados y el historial de los pedidos.
4. Notificaciones comunica al cliente cuando el pedido está listo.
