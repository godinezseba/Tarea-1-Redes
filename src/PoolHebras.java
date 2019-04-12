import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.IOException;

import java.lang.Thread;
import java.net.Socket;

public class PoolHebras{
    private final int nHebras;
    private final ListaHebras[] Hebras;
    private final LinkedBlockingQueue Cola;

    public PoolHebras(int nHebras){
        this.nHebras = nHebras;
        Cola = new LinkedBlockingQueue();
        Hebras = new PoolControl[nHebras];
        
        for (int i=0; i < this.nHebras; i++){
            Hebras[i] = new PoolControl();
            Hebras[i].start();  
        }
    }    
    public void ejecutar(Runnable proceso){
        synchronized(Cola){
            Cola.add(proceso);
            Cola.notify();
        }
    }

}