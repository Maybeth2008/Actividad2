# Actividad 2 — Autómatas Finitos para Análisis Léxico

**Materia:** Lenguajes Formales (C2566-SI2002)
**Universidad:** EAFIT — Escuela de Ciencias Aplicadas e Ingeniería
**Profesor:** Adolfo Andrés Castro Sánchez
**Referencia:** Aho et al., *Compilers: Principles, Techniques & Tools*, 2da ed., Figura 3.14, Sección 3.4.2

## Autores

- Nash Díaz Quessep
- Maybeth López Bedoya

## Descripción general

Este proyecto implementa los dos autómatas finitos que aparecen en la Figura 3.14 del libro *Compilers* de Aho. Estos autómatas son la base de la etapa de reconocimiento de tokens en un analizador léxico:

1. **Autómata 1 — Reconocedor de identificadores.** Acepta cualquier cadena que empiece con una letra y esté seguida por cero o más letras o dígitos.
2. **Autómata 2 — Reconocedor de la palabra reservada `then`.** Acepta la secuencia exacta `t-h-e-n` únicamente cuando va seguida de un carácter que no sea letra ni dígito (o del fin de la entrada).

Ambos autómatas implementan el comportamiento de **lookahead / retroceso** representado por el asterisco (`*`) en el diagrama: el carácter que dispara la transición de salida del loop no forma parte del lexema y se devuelve al buffer de entrada para el siguiente token.

## Entorno

| Componente | Versión |
|---|---|
| Sistema operativo | Ubuntu 24.04 LTS (también probado en Windows 11 y macOS) |
| Lenguaje de programación | Java 21 (compatible con Java 8 en adelante) |
| Herramienta de compilación | `javac` (JDK) |
| Editor | Visual Studio Code / IntelliJ IDEA |

No se requieren librerías externas. Solo el JDK estándar.

## Cómo ejecutar

### Requisitos previos

Instalar cualquier JDK (versión 8 o superior). Verificar con:

```bash
java -version
javac -version
```

### Compilación

Desde la raíz del proyecto, ejecutar:

```bash
javac Automatas.java
```

Esto genera el archivo `Automatas.class`.

### Ejecución

```bash
java Automatas
```

El programa va a:

1. Ejecutar una batería de casos de prueba predefinidos de manera automática.
2. Entrar en modo interactivo, donde se puede ingresar cualquier cadena y ver cómo ambos autómatas la procesan.

Para salir del modo interactivo, presionar ENTER con la línea vacía.

### Ejemplo de salida

```
=== Entrada: "then;" ===

Autómata 2 (then):
  [estado 0] --'t'--> [estado 1] --'h'--> [estado 2] --'e'--> [estado 3]
            --'n'--> [estado 4] --no letr/dig(';')--> [estado 5*] (retroceso)
→ Token: PALABRA_RESERVADA(then)
→ Lexema: "then"
→ Siguiente posición: 4 (resto: ";")
```

## Algoritmo

La implementación sigue el patrón clásico de **máquina de estados basada en switch** descrito en la Sección 3.4.2 del libro de referencia.

### Autómata 1 — Identificador

```
inicio → (9) --letra--> (10) --otro--> (11)*
                          ↺ letra | dígito
```

- **Estado 9** (inicial): espera una letra. Cualquier otro carácter rechaza la entrada.
- **Estado 10** (cuerpo): loop con letras y dígitos. Ante cualquier *otro* carácter, transita al estado 11 **sin consumirlo** (retroceso).
- **Estado 11** (aceptación, con `*`): emite el token identificador.

### Autómata 2 — Palabra reservada `then`

```
inicio → ( ) -t-> ( ) -h-> ( ) -e-> ( ) -n-> ( ) --no letr/dig--> ( )*
```

- Los estados 0 a 3 reconocen la secuencia literal `t`, `h`, `e`, `n`. Cualquier desviación rechaza.
- El estado 4 requiere un carácter que no sea letra ni dígito (o fin de entrada) para transitar al estado de aceptación 5. Esta condición es la que distingue a `then` de identificadores como `thenable`.

### Estrategia del analizador léxico

El método `analizar` prueba primero el autómata 2 (palabra reservada). Si este falla, pasa al autómata 1 (identificador). Esto refleja la regla de precedencia estándar en análisis léxico: **coincidencia más larga con prioridad a la palabra reservada**.

### Mecanismo de retroceso

Cada autómata retorna un objeto `Resultado` que contiene:

- `aceptado` — si la entrada produjo un token válido.
- `lexema` — únicamente los caracteres que pertenecen al token reconocido.
- `siguientePos` — el índice del carácter que disparó la transición de salida. Ese carácter **no se consume**, simulando el retroceso denotado por `*` en el diagrama.
- `traza` — una traza legible de las transiciones de estado, útil para visualización y depuración.

## Casos de prueba

El programa corre automáticamente los siguientes casos:

| Entrada | Resultado esperado |
|---|---|
| `then` | PALABRA_RESERVADA(then) |
| `then;` | PALABRA_RESERVADA(then), resto `";"` |
| `thenable` | IDENTIFICADOR `"thenable"` |
| `contador` | IDENTIFICADOR `"contador"` |
| `x1 + y` | IDENTIFICADOR `"x1"`, resto `" + y"` |
| `var_1` | IDENTIFICADOR `"var"`, resto `"_1"` (el guion bajo no está en la Figura 3.14) |
| `123abc` | Rechazado (no empieza con letra) |
| `the` | IDENTIFICADOR `"the"` (no completa `then`) |
| `""` | Entrada vacía |

## Estructura del proyecto

```
.
├── Automatas.java   # Código fuente con ambos autómatas y el driver
└── README.md        # Este archivo
```

## Video de demostración

Video explicativo (≤ 5 minutos) con la implementación, el algoritmo y una ejecución en vivo del programa:

**Enlace al video:** *(https://youtu.be/20Bq1-qJq68)*

## Referencias

Aho, Alfred V., Monica S. Lam, Ravi Sethi, y Jeffrey D. Ullman. *Compilers: Principles, Techniques, & Tools*. 2da ed. Boston: Pearson/Addison Wesley, 2007. Sección 3.4.2, Figura 3.14.


