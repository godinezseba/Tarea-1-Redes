// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        File file = null;

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

                int largoArch = entradaDatos.nextInt();
                
                fos = new FileOutputStream(mensajeterminal);
                out = new BufferedOutputStream(fos);
                in = new BufferedInputStream(socket.getInputStream());
                byte[] entrada = new byte[largoArch];

                for (int i = 0; i < entrada.length; i++) {
                    entrada[i] = (byte)in.read();
                }
                out.write(entrada);
                //out.flush();
            }
            else if(mensajeterminal.matches("^put [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){
                mensajeterminal = mensajeterminal.substring(4); // obtengo el nombre del archivo
                // envio el mensaje
                try {
                    file = new File(mensajeterminal);
                    int lengthArch = (int)file.length();
                    salidaDatos.println(lengthArch);

                    fis = new FileInputStream(mensajeterminal);
                    in = new BufferedInputStream(fis);
                    byte[] envio = new byte[lengthArch];
                    in.read(envio);

                    for (int i = 0; i < envio.length; i++) {
                        salidaDatos.write(envio[i]);
                    }
                    // termino de enviar el archivo
                } catch (Exception e) {
                    System.err.println("Error en el envio del archivo " + mensajeterminal);
                    e.printStackTrace();
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