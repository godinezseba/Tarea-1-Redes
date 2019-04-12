// entrada y salida
import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.IOException;

import java.lang.Thread;
import java.net.Socket;
public class Procesos implements Runnable{
    // variables que entrega el server
    final Scanner entradaDatos;
    final PrintStream salidaDatos;
    final Socket socket;
    // variaibles que manejo
    private InputStream in;
    private OutputStream out;
    private File archivo;
    
    public Procesos(Socket socket, Scanner entradaDatos, PrintStream salidaDatos){
        this.entradaDatos = entradaDatos;
        this.salidaDatos = salidaDatos;
        this.socket = socket;
        try {
            this.out = socket.getOutputStream();
        } catch (Exception e) {
            System.err.println("Error al crear la salida de datos");
        }
    }

    public void run() {
        String mensaje;
        byte[] bytes = new byte[16*1024];
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
                System.out.println(mensaje);

                if(mensaje.equals("Exit")){
                    this.socket.close();
                    break;
                }else if (mensaje.matches("^ls$")) {
                    File folder = new File("./filein");
                    File[] ListOfFiles = folder.listFiles();
                    //salidaDatos.println("Recibi tu ls");
                    for (int i = 0; i < ListOfFiles.length; i++){
                        if(ListOfFiles[i].isFile()){
                            salidaDatos.println("Archivo "+ ListOfFiles[i].getName());
                        }
                        else if(ListOfFiles[i].isDirectory()){
                            salidaDatos.println("Carpeta "+ListOfFiles[i].getName());
                        }
                    }
                }else if(mensaje.matches("^get [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){
                    mensaje = mensaje.substring(4);
                    try {
                        archivo = new File("./filein/"+mensaje);
                        in = new FileInput(archivo);
                        
                        int count;
                        while((count = in.read(bytes)) > 0){
                            out.write(bytes, o, count);
                        }
                        in.close();
                        archivo.close();
                        salidaDatos.println("Archivo " + mensaje + " enviado con exito!");
                    } catch (Exception e) {
                        System.err.println("Error al crear las variables de entrada y salida de archivos");
                        salidaDatos.println("Error al enviar el archivo");
                    }

                }else if(mensaje.matches("^delete [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){
                    mensaje = mensaje.substring(7);
                    System.out.println("archivo es "+mensaje);
                    File file = new File("./filein/"+mensaje);
                    if (file.delete()){ 
                        salidaDatos.println("Se elimino"+ mensaje);
                    }
                    else {
                        salidaDatos.println("Error al eliminar el archivo");
                    }
                    //salidaDatos.println("Recibi tu delete");
                }else if(mensaje.matches("^put [a-zA-Z0-9]*\\.[a-zA-Z0-9]*$")){
                    salidaDatos.println("Recibi tu put");
                }else{
                    
                    salidaDatos.println("Mensaje no valido");
                }
            } catch (Exception e) {
                System.err.println("No se pudo obtener el mensaje");
                //e.printStackTrace();
                break;
            }
        }
        try {
            this.entradaDatos.close();
            this.entradaDatos.close();
            this.out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}