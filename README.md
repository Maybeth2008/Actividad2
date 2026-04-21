# Implementación de Autómatas Finitos

## Sistema Operativo
Windows 11

## Lenguaje de Programación
Java

## Herramientas Utilizadas
- Java
- Online GDB 
- Terminal o Símbolo del sistema

## Instrucciones para ejecutar el programa
Guardar el archivo con el nombre "Automatas.java".

Abrir una terminal o consola en la carpeta donde está el archivo.

Compilar el programa con el siguiente comando:

javac Automatas.java

Ejecutar el programa con el comando:

java Automatas

El programa solicitará ingresar una cadena.
Ejemplo de entrada:

Ingrese una cadena: then

El programa indicará si la cadena es:
- Es palabra reservada (then)
- Es identificador
- No es válido

## Explicación del algoritmo
El programa implementa dos autómatas finitos deterministas (AFD).

El autómata procesa una cadena de entrada símbolo por símbolo.
Para cada símbolo leído, el programa verifica en qué estado se encuentra actualmente y determina el siguiente estado según las transiciones definidas.

El programa inicia en el estado inicial q0.

Durante la ejecución:

- Para la palabra reservada "then":
  El autómata recorre los estados q0, q1, q2, q3 y q4 leyendo los caracteres 't', 'h', 'e', 'n' respectivamente.
  Si la secuencia no coincide en algún punto, la cadena no es reconocida como palabra reservada.

- Para el identificador:
  El autómata valida que la cadena comience con una letra.
  Luego permite continuar con letras, dígitos o el carácter '_'.
  Si aparece un símbolo no permitido, la cadena es rechazada.

Este proceso se repite hasta que se hayan leído todos los símbolos de la cadena.

Al finalizar, el programa verifica el estado final:

- Si termina en el estado de aceptación del autómata "then", se reconoce como palabra reservada.
- Si termina en el estado de aceptación del autómata de identificador, se reconoce como identificador.
- En caso contrario, la cadena es considerada no válida.

## Autor
Trabajo realizado por Maybeth López Bedoya y Nash Díaz Quessep.
