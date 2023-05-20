import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int x = scan.nextInt();
        int y = scan.nextInt();

        System.out.println(multiply(x,y));
    }

    public static int multiply(int x, int y){
        return x*y;
    }
}