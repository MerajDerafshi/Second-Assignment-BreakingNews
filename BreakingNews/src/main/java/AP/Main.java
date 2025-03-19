package AP;

import java.util.Scanner;

public class Main {
    public static void main(String[] args ) {
        Scanner scanner = new Scanner(System.in);
        Infrastructure newsApp = new Infrastructure("980c8f4f4a934c799e5ebde0732d1cf3");
        System.out.println("Welcome to the news App ");
        System.out.println("Please press 1 to see the news list or 2 to Exit the program. ");
        while (true)
        {
            int option = scanner.nextInt();
            if (option == 1)
            {
                newsApp.displayNewsList();
                break;

            } else if (option == 2) {
                System.out.println("Goodbye");
                break;
            }
            else
            {
                System.out.println("Invalid ! Please try again");

            }
        }




    }
}