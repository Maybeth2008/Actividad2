import java.util.Scanner;

public class Automatas {

    // Autómata para identificador
    public static boolean esIdentificador(String s) {

        int estado = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            switch (estado) {

                case 0:
                    if (Character.isLetter(c)) {
                        estado = 1;
                    } else {
                        return false;
                    }
                    break;

                case 1:
                    if (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
                        estado = 1;
                    } else {
                        return false;
                    }
                    break;
            }
        }

        return estado == 1;
    }

    // Autómata para palabra reservada
    public static boolean esThen(String s) {

        int estado = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            switch (estado) {
                case 0:
                    if (c == 't') estado = 1;
                    else return false;
                    break;

                case 1:
                    if (c == 'h') estado = 2;
                    else return false;
                    break;

                case 2:
                    if (c == 'e') estado = 3;
                    else return false;
                    break;

                case 3:
                    if (c == 'n') estado = 4;
                    else return false;
                    break;

                default:
                    return false;
            }
        }

        return estado == 4;
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese una cadena: ");
        String entrada = sc.nextLine();

        if (esThen(entrada)) {
            System.out.println("→ Es palabra reservada (then)");
        } else if (esIdentificador(entrada)) {
            System.out.println("→ Es identificador");
        } else {
            System.out.println("→ No es válido");
        }

    }
}
