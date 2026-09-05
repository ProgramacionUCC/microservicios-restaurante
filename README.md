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

## Qué hay hecho hasta ahora — HU-02 Crear restaurante

### Qué hace
Permite crear un restaurante y asociarlo a un propietario ya existente. Sin un propietario válido no se puede crear el restaurante.

### Cómo lo hace
Cuando se quiere guardar un restaurante, el sistema hace validaciones en orden en `RestauranteService.java:17`:

1. **Revisa que todo esté lleno** — nombre, NIT, dirección, teléfono, urlLogo e idPropietario son obligatorios.
2. **Revisa el nombre** — no puede ser solo números (ej: `123` → error, `La Plazoleta` → ok).
3. **Revisa el NIT** — debe ser solo números.
4. **Revisa el teléfono** — solo números, puede empezar con `+` y máximo 13 caracteres en total (ej: `+573005698325`).
5. **Revisa el propietario** — el `idPropietario` debe coincidir con el `documentoDeIdentidad` de un usuario ya guardado con rol `PROPIETARIO` en `PropietarioRepository.java:14`.
6. **Lo guarda** — si todo pasa, lo deja en una lista en memoria en `RestauranteRepository.java:7`.

### Para qué se hace así
Para que no se creen restaurantes falsos o sin dueño, y para que el dato del teléfono y NIT siempre tenga formato correcto. Además, al validar contra el repositorio de propietarios se asegura la relación entre las dos piezas (Usuarios y Plazoleta) sin acoplarlas.

## Cómo está organizado el código

```
src/
  Main.java                          → ejemplo de uso, crea un propietario y luego un restaurante asociado
  model/Propietario.java             → solo guarda los datos del propietario, no hace validaciones
  model/Restaurante.java             → solo guarda los datos del restaurante (6 campos)
  service/PropietarioService.java    → el que revisa, protege y manda a guardar propietarios
  service/RestauranteService.java    → el que revisa y manda a guardar restaurantes
  repository/PropietarioRepository.java → el cajón donde se guardan los propietarios
  repository/RestauranteRepository.java → el cajón donde se guardan los restaurantes
  org/mindrot/BCrypt.java            → herramienta que protege las claves
```

- **model** es la ficha con los datos.
- **service** es el que piensa y decide si todo está bien.
- **repository** es el que solo guarda, no pregunta nada.

## Reglas que ya están funcionando

### HU-01 Propietario
- No se puede crear un propietario si falta algún dato.
- El correo debe tener forma de correo.
- El celular máximo 13 caracteres, solo números y +.
- El documento solo números.
- Debe ser mayor de edad.
- Todo propietario que se crea queda automáticamente con el rol `PROPIETARIO`.

### HU-02 Restaurante
- Todos los campos son obligatorios: nombre, NIT, dirección, teléfono, urlLogo e idPropietario.
- El nombre no puede ser solo números.
- El NIT debe ser solo numérico.
- El teléfono máximo 13, solo números y `+` opcional al inicio.
- El `idPropietario` debe existir en `PropietarioRepository` y tener rol `PROPIETARIO`.

## Estado actual

**HU-01 terminada** en la rama `feature/HU-01-crear-propietario` (pieza de Usuarios). Es la base para lo que sigue.

**HU-02 terminada** en la rama `feature/HU-02-crear-restaurante` (pieza de Plazoleta). Agrega `model/Restaurante.java:1`, `repository/RestauranteRepository.java:1`, `service/RestauranteService.java:17` y ejemplo en `Main.java:11`. No modifica nada de Propietario.

**Qué sigue:** HU-03 Crear plato, y así sucesivamente. Cada historia nueva agregará su parte aquí en este README.
