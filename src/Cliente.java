// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
// excepciones
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.IOException;
// sockets y hebras
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
public class Cliente {
    public static void main(String[] args) throws IOException {
        String mensaje, mensajeterminal;
        Socket socket = new Socket("localhost", 1234);
        Scanner inputterminal;
        

        // entrada y salida de datos
        Scanner entradaDatos = new Scanner(socket.getInputStream()); // entrada
        PrintStream salidaDatos = new PrintStream(socket.getOutputStream()); // salida
        // para los archivos
        InputStream in = null;
        OutputStream out = null;
        // abro el archivo
        File file = null;

        byte[] bytes = new byte[16*1024];

        // recibo un mensaje
        mensaje = entradaDatos.nextLine();
        System.out.println(mensaje);  

        // envio un mensaje
        salidaDatos.println("Cliente: Respuesta recibida");      
        inputterminal = new Scanner(System.in);
        // paso de mensajes
        while(true){
            System.out.print("> ");
            mensajeterminal = inputterminal.nextLine();
            salidaDatos.println(mensajeterminal);

            // veo como tratar la respuesta al comando
            if (mensajeterminal.equals("Exit")) {
                break;
            }
            else if(mensajeterminal.matches("^get [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){
                mensajeterminal = mensajeterminal.substring(4);

                // recibo el archivo
                in = socket.getInputStream();
                try {
                    out = new FileOutputStream(mensajeterminal);
                } catch (FileNotFoundException e) {
                    System.err.println("Error al solicitar el archivo");
                }

                int count;
                while((count = in.read(bytes)) > 0){
                    System.out.println(count);
                    out.write(bytes,0,count);
                }

                out.close();
                System.out.println("Recibi el archivo");
                // mensaje = entradaDatos.nextLine();
                // System.out.println(mensaje);
            }
            else if(mensajeterminal.matches("^put [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){
                mensajeterminal = mensajeterminal.substring(4); // obtengo el nombre del archivo
                // envio el mensaje
                try {
                    file = new File("./"+mensajeterminal);
                    in = new FileInputStream(file);
                    out = socket.getOutputStream();
                    
                    int count;
                    while((count = in.read(bytes)) > 0){
                        out.write(bytes, 0, count);
                    }

                    in.close();
                    salidaDatos.println("Archivo " + mensajeterminal + " enviado con exito!");

                } catch (Exception e) {
                    System.err.println("Error al crear las variables de entrada y salida de archivos");
                    salidaDatos.println("Error al enviar el archivo");
                }
            }
            else{
                mensaje = entradaDatos.nextLine();
                System.out.println(mensaje);
            }
        }
        
        System.out.println("Fin de la conexión");
        inputterminal.close();
        entradaDatos.close();
        socket.close();
    }
}