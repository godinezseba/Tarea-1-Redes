// entrada y salida
import java.util.Scanner;

import javafx.scene.chart.PieChart.Data;

import java.io.PrintStream;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
                        // variables a usar
                        archivo = new File(mensaje);
                        byte[] bytearray = new byte[(int)archivo.length()];
                        // entrada y salida
                        fis = new FileInputStream(archivo);
                        DataInputStream bis = new DataInputStream(new BufferedInputStream(fis));
                        bis.readFully(bytearray, 0, bytearray.length);

                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        // envio los datos
                        dos.writeLong(bytearray.length);                      
                        dos.write(bytearray, 0, bytearray.length);
                        dos.flush();
                        // cierro lo que no necesito
                        bis.close();
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
                    int bytesread;

                    DataInputStream entradad = new DataInputStream(socket.getInputStream());
                    fos = new FileOutputStream(mensaje);
                    long tamaño = entradad.readLong();
                    byte[] buffer = new byte[1024];
                    while (tamaño > 0 && (bytesread = entradad.read(buffer, 0, (int)Math.min(buffer.length, tamaño))) != -1) {
                        fos.write(buffer, 0, bytesread);
                        tamaño -= bytesread;
                        System.out.println(tamaño);
                    }
                    fos.close();
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