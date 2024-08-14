import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException{
        BigInt first;
        BigInt second;
        Scanner scan = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Please enter first big number please:");
                String firstNumber = scan.next();
                first = new BigInt(firstNumber);
                break;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while(true){
            try {
                System.out.println("Please enter second big number please:");
                String secondNumber = scan.next();
                second = new BigInt(secondNumber);
                break;
            }

            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Add result: " + first.plus(second));
        System.out.println("Subtract result: " + first.minus(second));
        System.out.println("Multiply result: " + first.multiply(second));

        try {
            System.out.println("Divide result: " + first.Divide(second));
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}