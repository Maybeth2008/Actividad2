import java.util.Scanner;
 
public class Automatas {
 
    
    static class Resultado {
        boolean aceptado;
        String lexema;
        int siguientePos;   // posición del carácter que sobró (retroceso *)
        String traza;       // traza de estados para mostrar en consola
 
        Resultado(boolean aceptado, String lexema, int siguientePos, String traza) {
            this.aceptado = aceptado;
            this.lexema = lexema;
            this.siguientePos = siguientePos;
            this.traza = traza;
        }
    }
 
    public static Resultado reconocerIdentificador(String entrada, int inicio) {
        int estado = 9;
        int i = inicio;
        StringBuilder lexema = new StringBuilder();
        StringBuilder traza = new StringBuilder();
        traza.append("  [estado ").append(estado).append("]");
 
        while (i < entrada.length()) {
            char c = entrada.charAt(i);
 
            if (estado == 9) {
                // Debe empezar por letra
                if (Character.isLetter(c)) {
                    estado = 10;
                    lexema.append(c);
                    traza.append(" --letra('").append(c).append("')--> [estado ").append(estado).append("]");
                    i++;
                } else {
                    return new Resultado(false, "", inicio,
                            traza.append(" ✗ no inicia con letra").toString());
                }
            } else if (estado == 10) {
                // Loop con letra o dígito; cualquier "otro" sale al estado 11
                if (Character.isLetter(c) || Character.isDigit(c)) {
                    lexema.append(c);
                    traza.append(" --").append(Character.isLetter(c) ? "letra" : "dígito")
                         .append("('").append(c).append("')--> [estado 10]");
                    i++;
                } else {
                    estado = 11;
                    traza.append(" --otro('").append(c).append("')--> [estado 11*] (retroceso)");
                    // No consumimos c: retroceso por el asterisco
                    return new Resultado(true, lexema.toString(), i, traza.toString());
                }
            }
        }
 
        // Llegamos al final de la entrada estando en 10 → aceptamos igual
        if (estado == 10) {
            traza.append(" --EOF--> [estado 11*] (fin de entrada)");
            return new Resultado(true, lexema.toString(), i, traza.toString());
        }
 
        return new Resultado(false, "", inicio, traza.toString());
    }
 
    
    public static Resultado reconocerThen(String entrada, int inicio) {
        char[] esperados = {'t', 'h', 'e', 'n'};
        int estado = 0;
        int i = inicio;
        StringBuilder lexema = new StringBuilder();
        StringBuilder traza = new StringBuilder();
        traza.append("  [estado ").append(estado).append("]");
 
        // Estados 0..3: leer t, h, e, n
        while (estado < 4 && i < entrada.length()) {
            char c = entrada.charAt(i);
            if (c == esperados[estado]) {
                lexema.append(c);
                estado++;
                traza.append(" --'").append(c).append("'--> [estado ").append(estado).append("]");
                i++;
            } else {
                return new Resultado(false, "", inicio,
                        traza.append(" ✗ se esperaba '").append(esperados[estado])
                             .append("' y llegó '").append(c).append("'").toString());
            }
        }
 
        // ¿Alcanzamos el estado 4 (leímos "then" completo)?
        if (estado != 4) {
            return new Resultado(false, "", inicio,
                    traza.append(" ✗ entrada incompleta").toString());
        }
 
        // Estado 4 → 5: requiere "no letra/dígito" o fin de entrada
        if (i >= entrada.length()) {
            traza.append(" --EOF--> [estado 5*]");
            return new Resultado(true, lexema.toString(), i, traza.toString());
        }
 
        char c = entrada.charAt(i);
        if (!Character.isLetter(c) && !Character.isDigit(c)) {
            traza.append(" --no letr/dig('").append(c).append("')--> [estado 5*] (retroceso)");
            return new Resultado(true, lexema.toString(), i, traza.toString());
        }
 
        // Si viene letra/dígito, "then" es en realidad prefijo de un identificador (ej. "thenable")
        return new Resultado(false, "", inicio,
                traza.append(" ✗ tras 'then' viene letra/dígito ('").append(c)
                     .append("') → no es palabra reservada").toString());
    }
 
    
    public static void analizar(String entrada) {
        System.out.println("\n=== Entrada: \"" + entrada + "\" ===");
 
        // Saltar espacios iniciales por comodidad
        int inicio = 0;
        while (inicio < entrada.length() && Character.isWhitespace(entrada.charAt(inicio))) {
            inicio++;
        }
        if (inicio >= entrada.length()) {
            System.out.println("(entrada vacía)");
            return;
        }
 
        // Intento 1: ¿es la palabra reservada "then"?
        System.out.println("\nAutómata 2 (then):");
        Resultado rThen = reconocerThen(entrada, inicio);
        System.out.println(rThen.traza);
 
        if (rThen.aceptado) {
            System.out.println("→ Token: PALABRA_RESERVADA(then)");
            System.out.println("→ Lexema: \"" + rThen.lexema + "\"");
            System.out.println("→ Siguiente posición: " + rThen.siguientePos +
                    (rThen.siguientePos < entrada.length()
                            ? " (resto: \"" + entrada.substring(rThen.siguientePos) + "\")"
                            : " (fin)"));
            return;
        }
 
        // Intento 2: ¿es un identificador?
        System.out.println("\nAutómata 1 (identificador):");
        Resultado rId = reconocerIdentificador(entrada, inicio);
        System.out.println(rId.traza);
 
        if (rId.aceptado) {
            System.out.println("→ Token: IDENTIFICADOR");
            System.out.println("→ Lexema: \"" + rId.lexema + "\"");
            System.out.println("→ Siguiente posición: " + rId.siguientePos +
                    (rId.siguientePos < entrada.length()
                            ? " (resto: \"" + entrada.substring(rId.siguientePos) + "\")"
                            : " (fin)"));
        } else {
            System.out.println("→ La entrada no es ni palabra reservada \"then\" ni identificador válido");
        }
    }
 
    public static void main(String[] args) {
        // Modo batch: casos de prueba automáticos
        String[] pruebas = {
                "then",           // palabra reservada pura
                "then;",          // palabra reservada + retroceso en ';'
                "thenable",       // identificador (NO es then, tiene letra después)
                "contador",       // identificador
                "x1 + y",         // identificador + sobrante
                "var_1",          // se acepta "var" y sobra "_1" (el _ no es letra ni dígito en Aho)
                "123abc",         // inválido (empieza con dígito)
                "the",            // identificador (no completa "then")
                ""                // vacío
        };
 
        System.out.println("========================================");
        System.out.println("  Autómatas Finitos — Figura 3.14 Aho");
        System.out.println("========================================");
        System.out.println("Ejecutando casos de prueba...");
 
        for (String p : pruebas) {
            analizar(p);
        }
 
        // Modo interactivo
        System.out.println("\n========================================");
        System.out.println("  Modo interactivo");
        System.out.println("========================================");
        System.out.println("Ingrese cadenas para analizar (ENTER vacío para salir):");
 
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("\n> ");
            if (!sc.hasNextLine()) break;
            String linea = sc.nextLine();
            if (linea.isEmpty()) break;
            analizar(linea);
        }
        sc.close();
        System.out.println("\nFin.");
    }
}
