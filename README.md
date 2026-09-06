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

## Qué hay hecho hasta ahora — HU-03 Crear plato

### Qué hace
Permite que el propietario de su restaurante cree platos dentro de ese restaurante. Cada plato nace activo y queda asociado al restaurante por su NIT.

### Cómo lo hace
Cuando se quiere guardar un plato, el sistema hace validaciones en orden en `PlatoService.java:19`:

1. **Revisa que todo esté lleno** — nombre, descripción, urlImagen, categoría e idRestaurante son obligatorios; además el precio debe venir (se valida aparte).
2. **Revisa el precio** — debe ser un número entero positivo mayor a 0 (`0` o negativo → error).
3. **Revisa que el restaurante exista** — busca el restaurante por NIT con `RestauranteRepository.java:22` `obtenerPorNit`. Si no existe → error.
4. **Revisa que sea el dueño** — el `idPropietarioAutenticado` (simula el usuario logueado hasta que exista HU-05) debe coincidir con el `idPropietario` del restaurante en `Restaurante.java:42`. Si no es el dueño → error.
5. **Lo guarda** — si todo pasa, lo deja en una lista en memoria en `PlatoRepository.java:8` y el plato queda con `activo = true` por defecto en `Plato.java:20`.

### Para qué se hace así
Para que nadie pueda crear platos en un restaurante que no existe o que no le pertenece, y para que el precio nunca quede en cero o negativo. Al usar el NIT como `idRestaurante` en `Plato.java:10` se mantiene la relación simple entre Plazoleta y Restaurante sin acoplar módulos.

## Qué hay hecho hasta ahora — HU-04 Modificar plato

### Qué hace
Permite que el propietario actualice **solo el precio y la descripción** de un plato que ya existe en su restaurante. Sirve para corregir valores errados o actualizar precios sin tener que borrar y volver a crear el plato.

### Cómo lo hace
Cuando se quiere modificar un plato, el sistema hace validaciones en orden en `PlatoService.java:50`:

1. **Revisa que nada venga vacío** — nombre del plato, NIT del restaurante, nueva descripción y propietario son obligatorios.
2. **Revisa el precio** — debe ser entero positivo mayor a 0 (`0` o negativo → error).
3. **Revisa que el restaurante exista** — busca por NIT con `RestauranteRepository.java:22` `obtenerPorNit`. Si no existe → error.
4. **Revisa que sea el dueño** — el `idPropietarioAutenticado` debe coincidir con el `idPropietario` del restaurante. Si no es el dueño → error.
5. **Revisa que el plato exista** — lo busca por nombre + NIT en `PlatoRepository.java:15` `obtenerTodos()`. Si no existe en ese restaurante → error.
6. **Lo actualiza** — solo hace `setPrecio` y `setDescripcion` en `Plato.java:50`, el resto (nombre, categoría, urlImagen, activo) no se toca.

### Para qué se hace así
Para que nadie pueda cambiar precios de un restaurante que no le pertenece y para que no se puedan modificar otros campos por error. Al cambiar solo dos setters se respeta el checklist del Trello y se mantiene la regla de negocio simple y segura.

## Qué hay hecho hasta ahora — HU-05 Agregar autenticación al sistema

### Qué hace
Permite que cualquier usuario (administrador, cliente, propietario o empleado) inicie sesión con correo y clave para acceder solo a lo que le corresponde según su rol.

### Cómo lo hace
Cuando se quiere iniciar sesión, el sistema hace validaciones en orden en `AutenticacionService.java:17`:

1. **Revisa que correo y clave no vengan vacíos** — ambos son obligatorios.
2. **Busca el usuario por correo** — recorre `PropietarioRepository.java:14` `getPropietarios()`. Si no existe → error “Usuario no encontrado”.
3. **Valida la contraseña** — compara la clave escrita con la guardada encriptada usando `BCrypt.checkpw`. Si no coincide → error “Contraseña incorrecta”.
4. **No limita intentos** — cada fallo solo informa, sin bloquear, para que pueda reintentar ilimitadamente.
5. **Garantiza permisos** — con `tienePermiso(usuario, rolRequerido)` en `AutenticacionService.java:30` verifica que `usuario.getRol()` coincida con el rol necesario para la acción.

### Para qué se hace así
Para que solo usuarios logueados usen el sistema y cada uno vea solo lo de su rol, sin acoplar la lógica de permisos a cada servicio. Al centralizarlo en AutenticacionService se protege el acceso desde un solo lugar.

## Cómo está organizado el código

```
src/
  Main.java                          → ejemplo de uso, crea propietario, restaurante, platos y prueba login con AutenticacionService (HU-05)
  model/Propietario.java             → solo guarda los datos del propietario, no hace validaciones
  model/Restaurante.java             → solo guarda los datos del restaurante (6 campos)
  model/Plato.java                   → solo guarda los datos del plato (6 campos + activo, nace en true)
  service/PropietarioService.java    → el que revisa, protege y manda a guardar propietarios
  service/RestauranteService.java    → el que revisa y manda a guardar restaurantes
  service/PlatoService.java          → el que revisa y manda a guardar/modificar platos (solo el dueño puede)
  service/AutenticacionService.java  → el que deja entrar con correo y clave y revisa permisos por rol (HU-05)
  repository/PropietarioRepository.java → el cajón donde se guardan los propietarios
  repository/RestauranteRepository.java → el cajón donde se guardan los restaurantes (ahora con obtenerPorNit)
  repository/PlatoRepository.java    → el cajón donde se guardan los platos
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

### HU-03 Plato
- Todos los campos son obligatorios: nombre, precio, descripción, urlImagen, categoría e idRestaurante (NIT).
- El precio debe ser entero positivo mayor a 0.
- El `idRestaurante` debe existir en `RestauranteRepository` vía `obtenerPorNit`.
- Solo el propietario dueño del restaurante (`Restaurante.getIdPropietario() == idPropietarioAutenticado`) puede crear platos en él.
- Todo plato se crea con `activo = true` por defecto.

### HU-04 Modificar plato
- Solo se pueden modificar **precio y descripción**, los demás campos no se tocan.
- El precio debe ser entero positivo mayor a 0, la descripción no puede venir vacía.
- El restaurante debe existir (`obtenerPorNit`) y el plato debe existir en ese restaurante (nombre + NIT).
- Solo el propietario dueño del restaurante puede modificarlo (`Restaurante.getIdPropietario() == idPropietarioAutenticado`).
- No se permiten modificar platos de otros restaurantes diferentes al propio.

### HU-05 Autenticación
- Inicio de sesión con **correo y clave**.
- Valida que el usuario exista y que la contraseña sea correcta (compara con `BCrypt.checkpw`).
- Número de intentos ilimitado (no bloquea, solo informa error cada vez).
- Una vez logueado, se garantiza que cada usuario solo puede hacer lo de su rol vía `tienePermiso(usuario, rolRequerido)` en `AutenticacionService.java:30`.

## Estado actual

**HU-01 terminada** en la rama `feature/HU-01-crear-propietario` (pieza de Usuarios). Es la base para lo que sigue.

**HU-02 terminada** en la rama `feature/HU-02-crear-restaurante` (pieza de Plazoleta). Agrega `model/Restaurante.java:1`, `repository/RestauranteRepository.java:1`, `service/RestauranteService.java:17` y ejemplo en `Main.java:11`. No modifica nada de Propietario.

**HU-03 terminada** en la rama `feature/HU-03-crear-plato` (pieza de Plazoleta). Agrega `model/Plato.java:1`, `repository/PlatoRepository.java:1`, `service/PlatoService.java:19` y corrige `repository/RestauranteRepository.java:22` con `obtenerPorNit` para que `PlatoService` pueda validar existencia y dueño. `Main.java:50` ahora demuestra 1 caso válido y 3 errores esperados (precio inválido, no es dueño, restaurante no existe).

**HU-04 terminada** en la rama `feature/HU-04-Modificar-plato` (pieza de Plazoleta). Agrega `Plato.java:50` `setPrecio/setDescripcion` y `PlatoService.java:50` `modificarPlato()` con 6 validaciones (vacíos, precio>0, restaurante existe, es dueño, plato existe, solo precio+desc). `Main.java:99` ahora demuestra 1 caso válido y 2 errores esperados (no es dueño, precio inválido). No toca ningún repository.

**HU-05 terminada** en la rama `feature/HU-05-agregar-autenticación-al-sistema` (pieza de Usuarios). Agrega `service/AutenticacionService.java:1` con `iniciarSesion(correo, clave)` (valida vacíos, busca por correo, compara `BCrypt.checkpw`, intentos ilimitados) y `tienePermiso(usuario, rol)`. `Main.java:127` ahora demuestra login OK, clave incorrecta, usuario no encontrado e intento ilimitado y permisos por rol.

**Qué sigue:** HU-06 y siguientes. Cada historia nueva agregará su parte aquí en este README.
