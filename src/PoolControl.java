public class PoolControl extends Thread{
    public void run(){
        Runnable proceso;
        while(true){
            //System.out.println("Ocurre");
            synchronized(Cola){
                while(Cola.isEmpty()){
                    try{
                        Cola.wait();
                    } catch(InterruptedException e){
                        System.out.println("Ocurrio un error en la espera de la cola "+ e.getMessage());
                    }
                }
                proceso = (Runnable)Cola.poll();
            }
            try{
                proceso.run();
            } catch(RuntimeException e){
                System.out.println("La ThreadPool ha sido interrumpida"+ e.getMessage());
            }
        }
    }
}