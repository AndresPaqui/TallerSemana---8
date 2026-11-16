# Taller BiblioLikes

## Enunciado

La biblioteca de una universidad desea ampliar el catálogo de libros que el equipo ha desarrollado en clases. Además de registrar y consultar libros, los usuarios podrán localizar un ejemplar, editar su información y expresar su interés mediante un botón «Me gusta». La biblioteca necesita un ranking de libros populares que admita empates.

Extiendan el proyecto existente utilizando **ArrayList, búsqueda lineal, búsqueda binaria, ordenamiento burbuja y Java Swing**. No es necesario comenzar otro sistema: adapten las clases y métodos que ya trabajaron.

El equipo tendrá un único repositorio de GitHub. Deben existir commits de código provenientes de **al menos tres cuentas distintas**, con al menos un commit significativo por cuenta. Todos los integrantes deben poder explicar su aporte. No se exigen ramas ni pull requests.

## Datos del libro

| Atributo | Tipo | Regla |
|---|---|---|
| `codigo` | `int` | Positivo, único e inmutable después del registro. |
| `titulo` | `String` | Obligatorio; puede coincidir con el título de otro libro. |
| `autor` | `String` | Obligatorio; puede repetirse. |
| `genero` | `String` | Obligatorio; seleccionado de un JComboBox. |
| `anio` | `int` | Desde 1 hasta el año actual. |
| `meGusta` | `int` | No negativo; empieza en 0 para nuevos registros. Aumenta con el botón Me gusta. |

Las categorías de este ejemplo son Programación, Bases de datos, Redes, Seguridad, Sistemas, Software, IA y Gestión. Pueden adaptar las etiquetas al modelo que ya tienen, conservando las reglas.

## Organización de las colecciones

La colección principal será `ArrayList<Libro> libros`. Debe permanecer ordenada por código ascendente para aplicar búsqueda binaria.

Para construir el ranking, creen una copia con `new ArrayList<>(libros)` y ordenen esa copia por `meGusta` descendente mediante burbuja. No ordenen la colección principal por me gusta: una búsqueda binaria por código sobre una lista organizada por popularidad puede devolver resultados incorrectos.

Los objetos Libro de las copias representan los mismos registros. Editar un libro debe actualizar su información en el catálogo; no se debe crear un registro distinto para mostrarlo en resultados o ranking. Ninguna búsqueda debe eliminar libros de la colección principal.

## Requerimientos funcionales

### R1 Cargar veinte registros al iniciar

- Precargar exactamente 20 libros de demostración utilizando `DatosIniciales.cargar()` o un método equivalente.
- Los códigos serán únicos y existirán valores repetidos en género, año y me gusta.
- Ordenar los datos por código con burbuja antes de habilitar la búsqueda binaria.
- Cargar los datos una sola vez por ejecución. Cambiar de pestaña, buscar o actualizar el ranking no debe volver a cargarlos ni duplicarlos.
- Mostrar la cantidad actual de libros. La información se conserva en memoria durante la sesión; no se exige base de datos ni archivos.

### R2 Registrar nuevos libros

- Crear un formulario con código, título, autor, género y año, más un botón Registrar.
- Validar campos obligatorios, código positivo, año permitido y código no repetido.
- Inicializar los me gusta del nuevo libro en 0; este valor no se ingresa manualmente en el formulario.
- Si el registro es válido, añadirlo al ArrayList y aplicar burbuja por código para mantener la condición de la búsqueda binaria.
- Si falla una validación, mostrar un mensaje y conservar los datos anteriores.

El usuario puede registrar un código menor que los existentes, por ejemplo 999. Por tanto, no se debe asumir que todos los nuevos códigos llegan al final del orden numérico.

### R3 Buscar coincidencias de texto y devolver un ArrayList

Implementar una búsqueda lineal que inspeccione título y autor y devuelva todas las coincidencias parciales.

```java
public ArrayList<Libro> buscarCoincidencias(String texto)
```

- Ignorar diferencias entre mayúsculas y minúsculas; eliminar espacios al inicio y al final de la consulta.
- Un libro coincide si el fragmento aparece en su título **o** en su autor. Si coincide en ambos, incluirlo una sola vez.
- Devolver un ArrayList nuevo; si no hay coincidencias, devolver uno vacío, no `null`.
- Rechazar una consulta vacía o formada solo por espacios.
- Mostrar los resultados en una `JList<Libro>` mediante `DefaultListModel<Libro>` y el número de coincidencias.
- Mantener el orden por código de los resultados y permitir seleccionar uno para mostrar su detalle.

Para esta práctica no es obligatorio ignorar tildes. Buscar `JAVA` debe comportarse igual que buscar `java`.

### R4 Buscar por código con búsqueda binaria y editar

Implementar una búsqueda binaria propia sobre la colección principal ordenada por código.

```java
public int buscarIndicePorCodigo(int codigo)
```

- Devolver el índice encontrado o `-1` si no existe.
- Rechazar códigos no positivos.
- Al encontrar un libro, mostrar sus datos en un formulario de edición y seleccionarlo en la JList de resultados.
- Permitir editar título, autor, género y año. El código no se modifica y me gusta no se escribe desde el formulario.
- Validar todos los datos antes de guardar: si algún campo es inválido, no realizar cambios parciales.
- Si no se encuentra un libro, mostrar un aviso y evitar que se edite o se dé me gusta al libro de una consulta anterior.
- Actualizar los datos visibles después de guardar, conservando el orden por código de la colección principal.

Los resultados de la búsqueda lineal también pueden seleccionarse para editar. Para identificar el registro que se va a guardar, utilizar su código único, no el índice de la JList ni el título.

### R5 Dar me gusta a un libro seleccionado

- Después de buscar por código o texto, seleccionar un libro y pulsar el botón **Me gusta +1**.
- Cada clic válido aumenta únicamente en uno el contador de ese libro.
- Mostrar el nuevo contador y actualizar las vistas afectadas.
- Si no hay selección válida, mostrar un aviso sin modificar ningún contador.
- Para esta simulación se permiten varios clics sobre un mismo libro. No hay cuentas de lectores, restricción de un voto por usuario ni operación «quitar me gusta».

El evento de selección de la JList solo muestra el detalle. El contador aumenta cuando se pulsa el botón, no al cambiar la selección ni al refrescar una lista.

### R6 Ordenar el ranking por un atributo repetido

Crear un método que devuelva los libros ordenados por popularidad:

```java
public ArrayList<Libro> obtenerRanking()
```

- Crear una copia del ArrayList principal y ordenarla con burbuja por me gusta descendente.
- Conservar todos los libros, incluso cuando sus contadores sean iguales.
- En los empates, mostrar código ascendente. Una forma de lograrlo es partir de la colección principal ordenada por código y utilizar burbuja estable: intercambiar solo cuando el elemento izquierdo tiene menos me gusta que el derecho.
- Mostrar código, título y contador en una JList de ranking.
- Actualizar el ranking al entrar en la pestaña o pulsar Actualizar. Debe reflejar los registros, cambios y clics realizados durante la sesión.
- Volver a buscar por código después de consultar el ranking debe seguir funcionando correctamente.

## Interfaz requerida con JTabbedPane

| Pestaña | Controles mínimos | Comportamiento |
|---|---|---|
| Registro | JTextField para código, título y autor; JComboBox para género; JSpinner para año; botón Registrar; total de libros. | Registrar y validar datos nuevos. |
| Búsquedas y edición | Campo de código y botón Buscar por código; campo de texto y botón Buscar coincidencias; JList de resultados; formulario de edición; botones Guardar cambios y Me gusta +1; contador visible. | Buscar, seleccionar un registro, editarlo y aumentar sus me gusta. |
| Ranking | JList de libros con sus contadores; botón Actualizar. | Mostrar popularidad descendente sin alterar el orden del catálogo principal. |

Después de registrar, editar o dar me gusta, refrescar los datos correspondientes sin reinicializar el catálogo. Si una edición hace que un libro deje de cumplir la búsqueda de texto activa, retirarlo de esos resultados al recalcular la consulta y limpiar la selección si corresponde.

## Organización sugerida del código

| Clase | Responsabilidad |
|---|---|
| `Libro` | Datos del libro y operaciones sobre sus atributos. |
| `CatalogoLibros` | ArrayList principal, validaciones, búsquedas, ediciones, me gusta y ordenamientos. |
| `DatosIniciales` | Construir los 20 libros de demostración. |
| `Ventana` o clase existente de interfaz | JTabbedPane, controles, selección y mensajes. |

Firmas orientativas adicionales:

```java
public void registrar(Libro libro)
public void editarLibro(int codigo, String titulo, String autor,
                        String genero, int anio)
public void darMeGusta(int codigo)
public void ordenarPorCodigoBurbuja()
```

Mantener los algoritmos en el modelo, separados de los eventos Swing. Los alumnos pueden adaptar nombres a su proyecto, pero deben conservar el comportamiento solicitado.

Para los algoritmos evaluados, no utilizar `Collections.sort()`, `List.sort()`, `Arrays.sort()`, `stream().sorted()` ni `Collections.binarySearch()`. El propósito es aplicar los métodos de burbuja y búsqueda binaria desarrollados en clase.

## Registros de demostración

Todos los libros, autores y años de esta tabla son datos ficticios para el ejercicio.

| Código | Título | Autor | Género | Año | Me gusta |
|---|---|---|---|---|---|
| 1001 | Fundamentos de Java | Ana Torres | Programación | 2021 | 5 |
| 1002 | Estructuras de datos | Luis Vega | Programación | 2020 | 3 |
| 1003 | Java para principiantes | Carla Mora | Programación | 2022 | 5 |
| 1004 | Diseño de bases de datos | Pedro León | Bases de datos | 2019 | 0 |
| 1005 | Algoritmos y complejidad | Sofía Paz | Programación | 2023 | 7 |
| 1006 | Programación orientada a objetos | Diego Ruiz | Programación | 2021 | 3 |
| 1007 | Introducción a redes | Elena Ramos | Redes | 2020 | 1 |
| 1008 | Desarrollo de interfaces Swing | Pablo Soto | Programación | 2024 | 7 |
| 1009 | Seguridad informática | Laura Díaz | Seguridad | 2022 | 0 |
| 1010 | Bases de datos relacionales | Marco Vera | Bases de datos | 2020 | 2 |
| 1011 | Java y colecciones | Daniela Cruz | Programación | 2025 | 5 |
| 1012 | Sistemas operativos | Andrés Luna | Sistemas | 2021 | 2 |
| 1013 | Ingeniería de software | Gabriela Ríos | Software | 2023 | 4 |
| 1014 | Pruebas de software | Mateo Peña | Software | 2022 | 1 |
| 1015 | Modelado con UML | Valeria Sol | Software | 2024 | 4 |
| 1016 | Inteligencia artificial | Nicolás López | IA | 2025 | 7 |
| 1017 | Gestión de proyectos | Paula Reyes | Gestión | 2021 | 0 |
| 1018 | Aplicaciones con Java | Camila Vega | Programación | 2023 | 2 |
| 1019 | Arquitectura de software | Tomás León | Software | 2024 | 3 |
| 1020 | Ciberseguridad aplicada | Isabel Rojas | Seguridad | 2025 | 1 |

Se incluye `DatosIniciales.java` con estos veinte registros quemados y una clase `Libro.java` de referencia para integrarlos. Pueden adaptar el constructor a la clase del proyecto existente. La carga entrega los datos desordenados intencionalmente; el equipo debe aplicar burbuja por código al iniciar.

## Casos de comprobación

| Caso | Acción | Resultado esperado |
|---|---|---|
| 1 | Iniciar y cambiar varias veces de pestaña. | Exactamente 20 registros, sin duplicaciones. |
| 2 | Intentar registrar otro código 1001. | Error; cantidad y libro original sin cambios. |
| 3 | Registrar código 999 con datos válidos. | Total 21; queda antes que 1001 en la colección principal y la búsqueda binaria lo encuentra. |
| 4 | Buscar `JAVA` antes de editar o agregar registros. | ArrayList con 1001, 1003, 1011 y 1018. |
| 5 | Buscar `bases` con los datos iniciales. | ArrayList con 1004 y 1010. |
| 6 | Buscar `vega` con los datos iniciales. | ArrayList con 1002 y 1018, por coincidencia en autor. |
| 7 | Buscar texto inexistente. | ArrayList vacío y mensaje; ninguna selección anterior permanece activa. |
| 8 | Buscar 1015 y editar su título con datos válidos. | Se actualiza ese registro; código y me gusta se conservan. |
| 9 | Intentar editar con autor vacío. | Error y ningún atributo cambia parcialmente. |
| 10 | Consultar el ranking inicial. | Primeros: 1005, 1008 y 1016, todos con 7. Permanecen los 20 libros. |
| 11 | Buscar 1001 y pulsar tres veces Me gusta +1. | Pasa de 5 a 8; al actualizar el ranking queda primero. |
| 12 | Después de ordenar el ranking, buscar 1004 mediante binaria. | Se encuentra; la colección principal continúa ordenada por código. |
| 13 | Pulsar Me gusta +1 sin un libro seleccionado. | Aviso; no cambia ningún contador. |
| 14 | Buscar 9999 después de consultar un libro existente. | No encontrado; Guardar y Me gusta no deben afectar el libro anterior. |

Los casos que indican «datos iniciales» deben ejecutarse en una sesión nueva o antes de modificar ese conjunto.

## Distribución del trabajo (seis participantes)

Cada participante debe implementar y explicar su parte. Coordinen primero las firmas de los métodos compartidos, especialmente los de `Lista`, para integrar sin duplicar lógica. Aunque el mínimo de entrega es de tres cuentas distintas, procuren que cada participante registre al menos un commit significativo desde su propia cuenta.

| Nombre | Responsabilidad | Entregables y coordinación |
|---|---|---|
| Steven Vaca | Adaptar el modelo `Libro` al catálogo BiblioLikes. | Mantener código inmutable, título, autor, género, año y me gusta; completar constructor, getters y operaciones necesarias. Asegurar que `Ventana` y `DatosIniciales` puedan crear y mostrar el mismo modelo. |
| Andrés Paqui | Preparar la carga inicial y el registro en `Lista`. | Integrar los 20 libros de `DatosIniciales` una sola vez, mantener el total visible y completar el registro con código positivo y único. Conservar la lista ordenada por código usando burbuja, incluso al agregar un código menor. |
| Rafaela Ludeña | Implementar y comprobar la búsqueda lineal por texto. | Completar `buscarCoincidencias` para buscar fragmentos en título o autor sin distinguir mayúsculas, rechazar consultas vacías y devolver un `ArrayList` nuevo sin duplicados. |
| Kevin Quinatoa | Completar la búsqueda binaria y la edición de libros. | Implementar la búsqueda por código sobre la lista ordenada; permitir editar título, autor, género y año sin cambiar código ni me gusta. Validar todos los campos antes de modificar para evitar cambios parciales. |
| Valeria Cáceres | Adaptar `Ventana.java` y `Ventana.form` al programa BiblioLikes. | Reemplazar los campos, categorías y pestañas del formulario heredado por las requeridas para Registro, Búsquedas y edición, y Ranking. Conectar los controles a los métodos acordados, mostrar validaciones y refrescar las vistas sin recargar el catálogo. |
| Jostin Saca | Implementar Me gusta y ranking, y verificar la integración. | Incrementar solo el libro seleccionado; crear una copia para ordenar por me gusta descendente con burbuja estable y desempate por código ascendente. Comprobar los casos de búsqueda, edición, selección, ranking y que la búsqueda binaria siga funcionando tras consultar el ranking. |

Orden sugerido de integración: acordar primero el modelo y las firmas de `Lista`; integrar carga/registro y búsquedas; después edición, Me gusta y ranking; finalmente conectar y probar la interfaz Swing. Los cambios de varias personas en `Lista.java` deben coordinarse para no sobrescribir aportes.

### Tutorial para cada participante

#### Steven Vaca: modelo `Libro`

1. Revisa `Libro.java` y compáralo con los datos que exige este taller. El objeto debe guardar código, título, autor, género, año y contador de me gusta.
2. Conserva el código como inmutable: se asigna al construir el libro y no se cambia al editarlo. Incluye un constructor compatible con los seis datos usados por `DatosIniciales.cargar()`. Para libros nuevos, el registro debe inicializar los me gusta en 0.
3. Proporciona getters para consultar los atributos y operaciones controladas para editar título, autor, género y año, además de incrementar los me gusta. No agregues un setter para cambiar el código.
4. Implementa `toString()` para que la JList pueda mostrar al menos código, título y contador. Coordina los nombres del constructor y métodos con quienes trabajan en `Lista.java` y `Ventana.java`.
5. Comprueba que las 20 llamadas de `DatosIniciales.java` compilen y que editar un libro no cambie su código ni sus me gusta.

#### Andrés Paqui: carga inicial y registro

1. Revisa `Lista.java` y `DatosIniciales.java`. Define con el equipo cómo se obtendrá el catálogo y evita crear una segunda lista independiente en la ventana.
2. Carga `DatosIniciales.cargar()` una sola vez cuando se inicia la aplicación. Ordena la colección principal por código con el método burbuja existente antes de usar la búsqueda binaria.
3. Completa el registro: valida que el libro y sus campos obligatorios sean válidos, que el código sea positivo y que no exista otro con ese código. Si la validación falla, informa el error sin cambiar la lista.
4. Al registrar correctamente, agrega el libro, vuelve a ordenar por código con burbuja y actualiza la cantidad que ve el usuario. No supongas que los nuevos códigos son mayores que los existentes.
5. Comprueba que el catálogo empiece con 20 libros, que un código duplicado no se agregue y que registrar el código 999 lo ubique al inicio sin romper la búsqueda binaria.

#### Rafaela Ludeña: búsqueda lineal por texto

1. En `Lista.java`, completa o reemplaza la búsqueda actual por un método como `buscarCoincidencias(String texto)` que devuelva `ArrayList<Libro>`.
2. Antes de recorrer el catálogo, quita espacios al principio y al final y convierte la consulta y los títulos/autores a minúsculas para comparar sin distinguir mayúsculas. Una consulta vacía o con solo espacios debe rechazarse con un mensaje claro.
3. Recorre la colección principal una sola vez. Agrega cada libro si el fragmento aparece en el título o en el autor; utiliza una condición única para que un mismo libro no aparezca dos veces. Devuelve una lista nueva y nunca `null`.
4. No elimines ni reordenes libros del catálogo al buscar. La búsqueda lineal debe conservar el orden por código que ya tiene la colección principal.
5. Comprueba las búsquedas `JAVA`, `bases` y `vega` con los datos iniciales, una búsqueda sin resultados y una consulta vacía. Confirma los resultados esperados en los casos de comprobación.

#### Kevin Quinatoa: búsqueda binaria y edición

1. Reutiliza en `Lista.java` el método binario sobre la colección principal ordenada por código. Debe devolver el índice del libro o `-1`, y rechazar códigos no positivos.
2. Al editar, identifica el libro por su código único. Valida primero todos los valores nuevos: título, autor, género y año entre 1 y el año actual. No cambies ningún atributo hasta que todas las validaciones hayan pasado.
3. Mantén el código y los me gusta fuera del formulario de edición. Después de guardar, modifica el mismo objeto del catálogo; no crees otro libro ni alteres el orden por código.
4. Si el código no existe, la interfaz debe avisar y limpiar/desactivar la selección anterior para que guardar o dar Me gusta no afecte otro libro.
5. Comprueba que se puede encontrar el código 1015, editarlo sin modificar su código o contador y que un año o autor inválidos no dejen cambios parciales. Prueba también un código inexistente después de una búsqueda válida.

#### Valeria Cáceres: adaptación de la ventana Swing

1. Abre `Ventana.form` y `Ventana.java` y ubica los controles del formulario heredado. Actualmente usa ISBN, categorías distintas y pestañas de ingreso/listado; compáralos con la interfaz de tres pestañas especificada arriba.
2. Adapta el formulario para Registro; Búsquedas y edición; y Ranking. Agrega o renombra los campos y botones requeridos, utiliza tipos genéricos como `JList<Libro>` y `DefaultListModel<Libro>` cuando corresponda, y configura el género y los límites del año.
3. Conecta cada botón al método acordado del catálogo. La ventana debe recibir o compartir la instancia de `Lista` que contiene los 20 libros; no debe crear ni recargar el catálogo cada vez que se cambia de pestaña.
4. Muestra mensajes de validación y resultados, actualiza el total y refresca las listas después de registrar, buscar o editar. Al seleccionar un resultado solo muestra sus datos; no incrementa los me gusta.
5. Comprueba que la aplicación arranca con 20 libros, que cada pestaña presenta su función y que los controles llaman al catálogo compartido. Integra los métodos de Me gusta y ranking con Jostin Saca.

#### Jostin Saca: Me gusta, ranking e integración

1. Implementa en el catálogo una operación que reciba el código del libro seleccionado, lo encuentre y aumente su contador en uno. Si no existe o no hay selección válida, avisa sin cambiar ningún libro.
2. Para obtener el ranking, crea una copia con `new ArrayList<>(...)`; no ordenes la colección principal. Aplica burbuja descendente por me gusta y, para conservar el desempate por código, intercambia solo cuando el contador izquierdo sea menor que el derecho.
3. Conecta el botón **Me gusta +1** y la pestaña Ranking: muestra código, título y contador, y actualiza el ranking al entrar en la pestaña o pulsar Actualizar.
4. Coordina con Valeria Cáceres para refrescar el detalle y las listas sin perder la selección de manera incorrecta. Después de una edición que afecte una búsqueda de texto, vuelve a calcular los resultados y limpia la selección si el libro ya no coincide.
5. Ejecuta los casos de comprobación con el equipo: ranking inicial con sus empates, tres clics sobre 1001, ausencia de selección, registro y edición, y búsqueda binaria después de consultar el ranking. Confirma que siguen apareciendo los 20 libros en el catálogo y que su orden por código no cambia.

## Trabajo en GitHub y entrega

1. Crear **un repositorio por equipo**, accesible para el docente.
2. Realizar aportes de código desde **al menos tres cuentas distintas**. Cada integrante debe tener como mínimo un commit significativo y atribuible a su cuenta.
3. Trabajar por turnos o con el procedimiento que conocen para subir código; no es obligatorio utilizar ramas ni pull requests.
4. Entregar el enlace, el código integrado, capturas de las tres pestañas y evidencia de los casos de comprobación.
5. Completar la tabla de aportes y preparar una demostración de búsqueda, edición, clics de me gusta y ranking.

| Nombre | Usuario GitHub | Funcionalidad desarrollada | Enlace al commit |
|---|---|---|---|
| Steven Vaca | Por completar | Modelo `Libro` | Por completar |
| Andrés Paqui | Por completar | Carga inicial y registro | Por completar |
| Rafaela Ludeña | Por completar | Búsqueda lineal | Por completar |
| Kevin Quinatoa | Por completar | Búsqueda binaria y edición | Por completar |
| Valeria Cáceres | Por completar | Adaptación e integración de Swing | Por completar |
| Jostin Saca | Por completar | Me gusta, ranking y pruebas de integración | Por completar |

Verificar que GitHub asocie los commits a sus cuentas; si utilizan Git local, el correo configurado debe estar asociado a la cuenta correspondiente. El número de commits no sustituye la explicación del aporte.

## RUBRICA

1. Funcionamiento **cualquier integrante ejecuta**, 3Puntos.
2. Intefaz realizada manualmente por estudiantes **cualquier integrante ejecuta**, 3Puntos.
3. Github **cualquier integrante explica**, 4Puntos.


#   T a l l e r S e m a n a - - - 8  
 