import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int number, temp;

        Socket s = new Socket("127.0.0.1", 1342);

        Scanner sc1 = new Scanner(s.getInputStream());
        System.out.println("Enter any number");
        number = sc.nextInt();
        PrintStream p = new PrintStream(s.getOutputStream());
        p.println(number);
        temp = sc1.nextInt();
        System.out.println(temp);

    }
}