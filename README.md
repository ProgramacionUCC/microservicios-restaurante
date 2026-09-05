# Plazoleta de Comidas — Microservicios

> Sistema de plazoleta con 4 microservicios desacoplados vía REST. Proyecto académico con Git Flow, roles y reglas de negocio reales.

## Qué hace el sistema

Un cliente hace un pedido a un restaurante, el pedido pasa por estados y se le avisa por SMS con un PIN para retirarlo. Cada microservicio es independiente.

```
Cliente → Usuarios (login/roles) → Plazoleta (restaurantes/platos/pedidos) → Trazabilidad (Pendiente → En preparación → Listo → Entregado) → Notificaciones (SMS/PIN)
```

## Arquitectura — 4 microservicios

| # | Servicio | Qué hace | HU clave |
|---|----------|----------|----------|
| 1 | **Usuarios** | Registro, login, bcrypt, roles (Admin/Propietario/Empleado/Cliente) | HU-01 Crear propietario |
| 2 | **Plazoleta** | Restaurantes, platos, pedidos (un pedido activo por cliente, todo del mismo restaurante) | HU-02 Restaurante, HU-03 Plato, HU-11 Pedido |
| 3 | **Trazabilidad** | Estados + asignación a empleado + métricas (tiempo, ranking) | Asignarse, cambiar estado, cancelar |
| 4 | **Notificaciones** | SMS cuando el pedido está Listo + PIN de entrega | Notificar pedido listo |

## Estructura del código (HU-01 actual)

```
src/
  Main.java                          → demo del flujo
  model/Propietario.java             → datos (7 campos + rol PROPIETARIO)
  repository/PropietarioRepository.java → lista en memoria (solo guarda)
  service/PropietarioService.java    → valida + encripta con bcrypt
  org/mindrot/BCrypt.java            → librería
```

**Flujo limpio en `PropietarioService.java`:**
```java
validarObligatorios(p);  // 7 campos llenos
validarFormato(p);       // email, celular max 13, documento solo numeros, 18+
encriptarClave(p);       // BCrypt.hashpw -> $2a$10$...
repository.guardarPropietario(p);
```

## Cómo correrlo

Requisitos: Java 26 (Temurin), PowerShell.

```powershell
javac -d out src/model/Propietario.java src/repository/PropietarioRepository.java src/service/PropietarioService.java src/org/mindrot/BCrypt.java src/Main.java
java -cp out Main
```

Salida:
```
Propietario{nombre='Carlos', correo='carlos@mail.com', rol='PROPIETARIO'}
Clave guardada: $2a$10$...
```

Probar errores: cambia en `Main.java` el correo a `sin-arroba`, celular a `+57300569832599` (>13), documento a `12A`, o fecha a `2020-01-01` (<18) y vuelve a correr.

## Reglas de negocio (HU-01)

| Regla | Válido | Inválido |
|-------|--------|----------|
| 7 campos + bcrypt | todos llenos | `""` → error |
| Email + celular | `a@mail.com`, `+573005698325` (13) | `sin-arroba`, `+57300569832599` |
| Documento | `12345678` | `12A` |
| Rol | automático `PROPIETARIO` | — |
| Edad | `1990-05-10` | `2020-01-01` → menor |

## Git Flow y commits

Ramas: `main` (estable) / `develop` (integración) / `feature/HU-XX-nombre` / `release/sprint-x` / `hotfix/...`

Formato commit: `tipo(modulo): descripción [HU-xx]` — tipos `feat, fix, docs, test, refactor, chore`

Ejemplo: `feat(usuarios): crear endpoint crear propietario [HU-01]`

Regla: 3 commits/semana en días distintos, todo vía PR a `develop`, nunca directo a `main`.

## Estado

Sprint 1 — HU-01 terminada en `feature/HU-01-crear-propietario` (Microservicio Usuarios). Siguientes: HU-02 restaurante, HU-03 plato.
