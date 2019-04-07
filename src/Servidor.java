// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{
    public static void main(String[] args) throws IOException {
        // recibo el mensaje
        String mensaje;
        // creo los sockets
        ServerSocket ss = new ServerSocket(1234); 
        Socket cs = new Socket(); // comienza el socket y espera clientes

        // entrada de un cliente
        System.out.println("Esperando...");
        cs = ss.accept();
        System.out.println("Cliente en línea");

        // entrada y salida de datos
        Scanner entradaDatos = new Scanner(cs.getInputStream()); // entrada
        PrintStream salidaDatos = new PrintStream(cs.getOutputStream()); // salida
        
        // leo un mensaje
        mensaje = entradaDatos.nextLine();
        System.out.println(mensaje);

        // envio un mensaje
        salidaDatos.println("Servidor: Hola Cliente");

        System.out.println("Fin de la conexión");

        ss.close();
    }
}