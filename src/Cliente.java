// entrada y salida
import java.io.PrintStream;
import java.util.Scanner;

import java.io.IOException;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws IOException {
        String mensaje;
        Socket cs = new Socket("127.0.0.1", 1234);

        // entrada y salida de datos
        Scanner entradaDatos = new Scanner(cs.getInputStream()); // entrada
        PrintStream salidaDatos = new PrintStream(cs.getOutputStream()); // salida

        // envio un mensaje
        salidaDatos.println("Cliente: Hola soy un cliente!");

        // recibo un mensaje
        mensaje = entradaDatos.nextLine();
        System.out.println(mensaje);        
        
        System.out.println("Fin de la conexión");
        cs.close();
    }
}