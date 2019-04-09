// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
public class Servidor{
    public static void main(String[] args) throws IOException {
        // recibo el mensaje
        String mensaje;
        ServerSocket ss = null;

        // creo los sockets
        try{
            ss = new ServerSocket(1234); 
        } catch(IOException e){
            System.err.println("No se pudo abrir el puerto");
            //exit(-1);
        }
        Socket cs = null;
        InputStream in = null;
        OutputStream out = null;

        System.out.println("Esperando...");

        // entrada de un cliente
        try {
            cs = ss.accept(); // comienza el socket y espera clientes
            System.out.println("Cliente en línea");
        } catch (Exception e) {
            System.err.println("Error en la entrada de un cliente");
        }

        // entrada y salida de datos
        Scanner entradaDatos = new Scanner(cs.getInputStream()); // entrada
        PrintStream salidaDatos = new PrintStream(cs.getOutputStream()); // salida
        try {
            in = cs.getInputStream();
        } catch (Exception e) {
            System.err.println("No se pudo obtener el input");
        }


        // leo un mensaje
        mensaje = entradaDatos.nextLine();
        System.out.println(mensaje);

        // envio un mensaje
        salidaDatos.println("Servidor: Hola Cliente");

        // creo el archivo a guardar
        try {
            out = new FileOutputStream("./../files/test2.txt");
        } catch (FileNotFoundException e) {
            try {
                File file = new File("./../files/test2.txt");
                file.createNewFile();
            } catch (Exception er) {
                System.err.println("Error al crear el archivo");
            }
        }

        byte[] bytes = new byte[16*1024];

        int count;
        while((count = in.read(bytes)) > 0){
            out.write(bytes, 0, count);
        }
        System.out.println("Fin de la conexión");

        out.close();
        in.close();
        entradaDatos.close();
        ss.close();
        cs.close();

        //ss.close();
    }
}