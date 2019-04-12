// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
// excepciones
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.IOException;
// sockets y hebras
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
public class Servidor{
    public static void main(String[] args) throws IOException{
        // creo las variables basicas a usar por el server
        Socket socket = null;
        Scanner entradaDatos = null;
        PrintStream salidaDatos = null;
        ServerSocket serversocket = null;
        PoolHebras piscina = null; // piscina de hebras

        // creo el socket
        try{
            serversocket = new ServerSocket(1234); 
        } catch(IOException e){
            System.err.println("No se pudo abrir el puerto");
            System.exit(-1);
        }

        // INICIO DEL THREADPOOL
        piscina = new PoolHebras(10);

        // mkdir filein (para guardar los archivos que ingreso)


        while (true) {
            System.out.println("Esperando...");

            try {
                socket = serversocket.accept(); // entrada de un cliente
                System.out.println("Cliente en línea" + socket.getRemoteSocketAddress().toString());

                // creo las variables de entrada y salida de datos
                entradaDatos = new Scanner(socket.getInputStream());
                salidaDatos = new PrintStream(socket.getOutputStream()); 

                // ahora la hebra trabaja con el cliente
                piscina.ejecutar(new Procesos(socket, entradaDatos, salidaDatos));

            } catch (Exception e) {
                System.err.println("Error en la entrada de un cliente");
                e.printStackTrace();
                socket.close();
            }
        }
        // termino del servidor
        //System.out.println("Fin de la ejecución");
        //serversocket.close();
    }
}