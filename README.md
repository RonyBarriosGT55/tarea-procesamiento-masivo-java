## Rony Alexander Barrios Borrayo

## Descripción del problema

El problema principal del programa es que procesa muchos clientes de forma poco eficiente.

La versión base carga todos los registros en memoria, lo que puede consumir demasiada RAM y provocar lentitud o errores como OutOfMemoryError.

Además, agrupa las campañas usando listas y búsquedas lineales, por lo que debe revisar elemento por elemento. Esto funciona con pocos datos, pero con millones de registros se vuelve lento.

También el campo JSON aumenta el consumo de memoria, especialmente si se guarda.

Por eso, el programa debe optimizarse usando una estructura más eficiente como `HashMap`.

### 1. ¿Por qué cargar todos los clientes en memoria puede ser un problema?

Porque el programa guarda todos los registros al mismo tiempo en la memoria RAM. Si hay millones de clientes y cada uno tiene muchos datos, la memoria puede llenarse y provocar errores como OutOfMemoryError.

---

### 2. ¿Por qué usar listas con búsqueda lineal no es eficiente?

Porque el programa debe revisar elemento por elemento hasta encontrar una campaña. Con pocos datos funciona, pero con muchos registros se vuelve lento porque hace demasiadas comparaciones.

---

### 3. ¿Qué estructura de datos sería más adecuada para agrupar campañas?

La estructura más adecuada es un `HashMap`, porque permite buscar y agrupar campañas usando una clave, sin recorrer toda una lista.

Ejemplo:

```java
HashMap<String, List<Cliente>> campanias = new HashMap<>();
