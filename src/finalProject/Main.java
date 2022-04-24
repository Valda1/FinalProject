package finalProject;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to our service!");
        System.out.println("______________________");
        System.out.println("Create a new email address? y/n");

        char newEmailAddress = 0;
        while (newEmailAddress != 'y' || newEmailAddress != 'n') {
            Scanner scanner = new Scanner(System.in);
            newEmailAddress = scanner.next().toLowerCase().charAt(0);

            if (newEmailAddress == 'y') {
                DB myData = new DB();
                myData.userRegistration();
                break;
            } else if (newEmailAddress == 'n') {
                System.out.println("Returning user? Do you want to log in? y/n");
                char choice = scanner.next().toLowerCase().charAt(0);
                if (choice == 'y') {
                    DB myData = new DB();
                    myData.userLogIn();
                    break;
                } else if (choice == 'n') {
                    System.out.println("Goodbye!");
                    break;
                }
            } else{
                System.out.println("Incorrect input! Try again!");
                break;
            }
        }

    }
}
