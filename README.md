# Plazoleta de Comidas — Microservicios

Proyecto de plazoleta donde cada parte funciona por separado y se hablan entre sí. La idea es que el sistema crezca sin enredarse.

> Este README cuenta lo que **ya está hecho en el código**. Se va actualizando en cada Historia de Usuario. Para el plan completo ver `CONTEXTO_PROYECTO.md`.

## Qué hace el proyecto

Una plazoleta con restaurantes. Un administrador registra a los dueños de los restaurantes, esos dueños luego podrán manejar su local, y más adelante los clientes harán pedidos que pasan por varios estados hasta entregarse. Todo está dividido en 4 piezas pequeñas para que cada una haga solo lo suyo.

## Qué hay hecho hasta ahora — HU-01 Crear propietario

### Qué hace
Permite que el administrador cree la cuenta de un propietario. Es el primer paso, sin esto no se pueden crear restaurantes después.

### Cómo lo hace
Cuando se quiere guardar un propietario, el sistema hace 4 pasos seguidos:

1. **Revisa que todo esté lleno** — nombre, apellido, documento, celular, fecha de nacimiento, correo y clave no pueden venir vacíos.
2. **Revisa que todo tenga buen formato** — el correo debe parecer un correo, el celular no puede pasar de 13 caracteres y solo lleva números y + al inicio, el documento solo lleva números y la persona debe ser mayor de 18 años.
3. **Protege la clave** — no guarda la clave tal cual, la transforma en un código irreconocible que no se puede volver a leer. Así si alguien ve la base de datos no ve las claves reales.
4. **Lo guarda** — lo deja en una lista en memoria. Más adelante esa lista será una base de datos, pero el resto del código no tendrá que cambiar.

### Para qué se hace así
Para que el código sea fácil de entender y de probar. Cada paso está separado, si algo falla se sabe exactamente dónde fue. Y para que desde el inicio las claves estén protegidas y los datos estén bien.

## Cómo está organizado el código

```
src/
  Main.java                          → ejemplo de uso, crea un propietario y lo guarda
  model/Propietario.java             → solo guarda los datos, no hace validaciones
  service/PropietarioService.java    → el que revisa, protege y manda a guardar
  repository/PropietarioRepository.java → el cajón donde se guardan
  org/mindrot/BCrypt.java            → herramienta que protege las claves
```

- **model** es la ficha con los datos.
- **service** es el que piensa y decide si todo está bien.
- **repository** es el que solo guarda, no pregunta nada.

## Reglas que ya están funcionando

- No se puede crear un propietario si falta algún dato.
- El correo debe tener forma de correo.
- El celular máximo 13 caracteres, solo números y +.
- El documento solo números.
- Debe ser mayor de edad.
- Todo propietario que se crea queda automáticamente con el rol `PROPIETARIO`.

## Estado actual

**HU-01 terminada** en la rama `feature/HU-01-crear-propietario` (pieza de Usuarios). Es la base para lo que sigue.

**Qué sigue:** HU-02 Crear restaurante, HU-03 Crear plato, y así sucesivamente. Cada historia nueva agregará su parte aquí en este README.
