// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.StringWriter;
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
// hebras y sockets
import java.lang.Thread;
import java.net.Socket;
public class Procesos implements Runnable{
    // variables que entrega el server
    final Scanner entradaDatos;
    final PrintStream salidaDatos;
    final Socket socket;
    
    public Procesos(Socket socket, Scanner entradaDatos, PrintStream salidaDatos){
        this.entradaDatos = entradaDatos;
        this.salidaDatos = salidaDatos;
        this.socket = socket;
    }

    public void run() {
        String mensaje;
        // variable para envio de archivos
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        File archivo;
        // handshake
        // envio un mensaje
        salidaDatos.println("Servidor: Hola Cliente");

        // leo un mensaje
        mensaje = entradaDatos.nextLine();
        System.out.println(mensaje);

        //ciclo mientras recibo mensajes
        while (true) {
            try {

                mensaje = entradaDatos.nextLine();
                System.out.println(socket.getRemoteSocketAddress().toString() + " " + mensaje);

                if(mensaje.equals("Exit")){
                    this.socket.close();
                    break;
                }
                else if (mensaje.equals("ls")) {
                    File folder = new File(".");
                    File[] ListOfFiles = folder.listFiles();
                    // System.out.println(String.valueOf(ListOfFiles.length));
                    // entrego la cantidad de mensajes que enviare para imprimirlos
                    salidaDatos.println(String.valueOf(ListOfFiles.length));
                    
                    for (int i = 0; i < ListOfFiles.length; i++){
                        if(ListOfFiles[i].isFile()){
                            salidaDatos.println("Archivo "+ ListOfFiles[i].getName());
                        }
                        else if(ListOfFiles[i].isDirectory()){
                            salidaDatos.println("Carpeta " + ListOfFiles[i].getName());
                        }
                    }
                } 
                else if(mensaje.matches("^get [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){ // comando get
                    mensaje = mensaje.substring(4); // obtengo el nombre del archivo
                    // envio el mensaje
                    try {
                        archivo = new File(mensaje);
                        int lengthArch = (int)archivo.length();
                        salidaDatos.println(lengthArch);

                        fis = new FileInputStream(mensaje);
                        in = new BufferedInputStream(fis);
                        byte[] envio = new byte[lengthArch];
                        in.read(envio);

                        for (int i = 0; i < envio.length; i++) {
                            salidaDatos.write(envio[i]);
                        }
                        // termino de enviar el archivo
                    } catch (Exception e) {
                        System.err.println("Error en el envio del archivo");
                        salidaDatos.println("Error al enviar el archivo " + mensaje);
                    }
                }
                else if(mensaje.matches("^delete [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){ // comando delete
                    mensaje = mensaje.substring(7);
                    //System.out.println("archivo es "+mensaje);
                    File file = new File("./"+mensaje);
                    if (file.delete()){ 
                        salidaDatos.println("Se elimino " + mensaje);
                    }
                    else {
                        salidaDatos.println("Error al eliminar el archivo " + mensaje);
                    }
                    //salidaDatos.println("Recibi tu delete");
                }
                else if(mensaje.matches("^put [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){ // comando put
                    mensaje = mensaje.substring(4);

                    int largoArch = entradaDatos.nextInt();
                
                    fos = new FileOutputStream(mensaje);
                    out = new BufferedOutputStream(fos);
                    in = new BufferedInputStream(socket.getInputStream());
                    byte[] entrada = new byte[largoArch];
    
                    for (int i = 0; i < entrada.length; i++) {
                        entrada[i] = (byte)in.read();
                    }
                    out.write(entrada);
                    out.flush();
                }else{ 
                    salidaDatos.println("Mensaje no valido: " + mensaje);
                }
            } catch (Exception e) {
                System.err.println("No se pudo obtener el mensaje");
                //e.printStackTrace();
                break;
            }
        }
        try {
            this.entradaDatos.close();
            this.salidaDatos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}