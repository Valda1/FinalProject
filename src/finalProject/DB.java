package finalProject;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB {

    final String dbURL = "jdbc:mysql://localhost:3306/java27";
    final String user = "root";
    final String password = "accenture27";

    private String emailAddress;
    private String pswrd;
    private String fullName;
    private String contactFullName;
    private String contactEmail;

    public void userRegistration() {
        User newUser = new User();
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(dbURL, user, password)) {

            //CHOOSING THE E-MAIL ADDRESS
            boolean matches = false;

            while (!matches) {
                System.out.println("Please choose your new e-mail address (________@mail.net): ");
                emailAddress = scanner.nextLine();
                newUser.setEmailAddress(emailAddress);

                Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
                Matcher matcher = pattern.matcher(emailAddress);
                matches = matcher.matches();

                PreparedStatement prepareStatement = conn.prepareStatement("SELECT * FROM users1 WHERE email_address = ?;");
                prepareStatement.setString(1, newUser.getEmailAddress());
                prepareStatement.executeQuery();

                ResultSet resultSet = prepareStatement.executeQuery();

                if (!resultSet.next()) {
                    System.out.println("You have chosen your e-mail address successfully!");
                    System.out.println("______________________");
                } else {
                    System.out.println("This e-mail address already exists. Try again!");
                    System.out.println(" ");
                    matches = false;
                }
            }

            //CHOOSING THE PASSWORD
            boolean matches1 = false;

            while (!matches1) {
                System.out.println("Please choose your password. Password must consist of 5 letters and 1 digit.");
                pswrd = scanner.nextLine();
                newUser.setPswrd(pswrd);

                Pattern pattern = Pattern.compile("[a-zA-Z]{5}[0-9]");
                Matcher matcher = pattern.matcher(newUser.getPswrd());
                matches1 = matcher.matches();
                if (matches1) {
                    System.out.println("You have chosen password successfully!");
                    System.out.println("______________________");
                } else {
                    System.out.println("Try another password!");
                    System.out.println(" ");
                }
            }

            //SETTING FULL NAME
            boolean matches2 = false;

            while (!matches2) {
                System.out.println("Please enter your full name: ");
                fullName = scanner.nextLine().toLowerCase().trim();
                newUser.setFullName(fullName);

                Pattern pattern = Pattern.compile("[a-zA-Z ]*");
                Matcher matcher = pattern.matcher(newUser.getFullName());
                matches2 = matcher.matches();
                if (matches2) {
                    System.out.println("You have set your full name successfully!");
                    System.out.println("______________________");
                } else {
                    System.out.println("Try again!");
                    System.out.println(" ");
                }
            }

            insertUserData(conn, emailAddress, pswrd, fullName);
            userMenu();


        }catch (SQLException e){
            System.out.println("Something went wrong! " + e);
        }
    }

    public void userLogIn() {

        String emailAddress;
        String pswrd;

        Scanner scanner = new Scanner(System.in);

        try(Connection conn = DriverManager.getConnection(dbURL,user,password)){

            do{
                System.out.println("Enter your e-mail address: ");
                emailAddress = scanner.nextLine().trim();
                System.out.println("Enter your password: ");
                pswrd = scanner.nextLine().trim();
            }while(checkLogInInfo(conn, emailAddress, pswrd) != 1);

            userMenu();

        } catch (SQLException e){
            System.out.println("Something went wrong! " + e);
        }

    }

    public static void insertUserData(Connection conn, String emailAddress, String pswrd, String fullName) throws SQLException {

        String sql = "INSERT INTO users1 (email_address, password, full_name) VALUES (?, ?, ?);";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, emailAddress);
        preparedStatement.setString(2, pswrd);
        preparedStatement.setString(3, fullName);

        int rowInserted = preparedStatement.executeUpdate();

        if (rowInserted > 0) {
            System.out.println("Your new e-mail address was created successfully!");
            System.out.println("______________________");
        } else {
            System.out.println("Something went wrong!");
        }


    }

    public static int checkLogInInfo(Connection conn, String emailAddress, String pswrd) throws SQLException {

        String sql = "SELECT count(*) FROM users1 WHERE email_address = ? AND password = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, emailAddress);
        preparedStatement.setString(2, pswrd);
        ResultSet resultSet = preparedStatement.executeQuery();

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
            if (count == 1) {
                System.out.println("Welcome to your e-mail!");
                System.out.println("______________________");
            } else if (count != 1) {
                System.out.println("Entered login or password is incorrect. Please try again!");
                System.out.println(" ");
            }
        }
        return count;
    }

    public void userMenu() {

        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(dbURL, user, password)) {

            boolean quit = false;
            int choice = 0;

            System.out.println("What would you like to do?");

            while (!quit) {
                printOptions();
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 0:
                        //print all options
                        printOptions();
                        break;
                    case 1:
                        //send new email
                        sendEmail();
                        break;
                    case 2:
                        //delete email
                        deleteEmail(conn);
                        break;
                    case 3:
                        //print sent emails
                        printSentEmails(conn);
                        break;
                    case 4:
                        //add contact
                        addContact();
                        break;
                    case 5:
                        //delete contact
                        deleteContact(conn);
                        break;
                    case 6:
                        //print contact list
                        printContacts(conn);
                        break;
                    case 7:
                        quit = true;
                        System.out.println("Goodbye! See you next time!");
                        break;


                }

            }

        }catch (SQLException e){
            System.out.println("Something went wrong: " + e);
        }


    }

    public static void printOptions () {
        System.out.println("\nPress");
        System.out.println("\t 0 - To print choice options");
        System.out.println("\t 1 - To send a new e-mail");
        System.out.println("\t 2 - To delete an e-mail");
        System.out.println("\t 3 - To print out sent e-mail list");
        System.out.println("\t 4 - To add a contact person to contact list");
        System.out.println("\t 5 - To delete a contact person from contact list");
        System.out.println("\t 6 - To print out contact list");
        System.out.println("\t 7 - To log out");
    }

    public void sendEmail() {

        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(dbURL, user, password)) {

            String sender;
            String receiver;
            String subject;
            String text;
            LocalDate localDate = LocalDate.now();
            String date = localDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            LocalTime now = LocalTime.now();
            String time = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();

            System.out.println("Enter your e-mail address: ");
            sender = scanner.next();
            System.out.println("Enter e-mail address of the receiver: ");
            receiver = scanner.next();
            System.out.println("Enter the subject of the e-mail: ");
            subject = scanner.next();
            System.out.println("Enter the text of your e-mail: ");
            text = scanner.next();
            System.out.println(" ");

            insertEmailData(conn, sender, receiver, subject, text, date, time);

        } catch (SQLException e){
            System.out.println("Something went wrong! " + e);
        }
    }

    public static void insertEmailData(Connection conn, String sender, String receiver, String subject, String text, String date, String time) throws SQLException{

        String sql = "INSERT INTO emails (sender, receiver, subject, email_text, date, time) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, sender);
        preparedStatement.setString(2, receiver);
        preparedStatement.setString(3, subject);
        preparedStatement.setString(4, text);
        preparedStatement.setString(5, date);
        preparedStatement.setString(6, time);


        int rowInserted = preparedStatement.executeUpdate();

        if (rowInserted > 0) {
            System.out.println("Your e-mail has been sent successfully!");
            System.out.println("______________________");
        } else {
            System.out.println("Something went wrong!");
        }



    }

    public static void deleteEmail(Connection conn) throws SQLException{

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter ID number of the e-mail that you want to delete: ");
        int emailID = scanner.nextInt();

        String sql = "DELETE FROM emails WHERE email_id = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, emailID);

        int rowDeleted = preparedStatement.executeUpdate();

        if(rowDeleted > 0){
            System.out.println("The e-mail was deleted successfully!");
        }else {
            System.out.println("Something went wrong!");
        }

    }

    public static void printSentEmails(Connection conn) throws SQLException{

        String sql = "SELECT * FROM emails;";

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()){

            int emailID = resultSet.getInt(1);
            String sender = resultSet.getString(2);
            String receiver = resultSet.getString(3);
            String subject = resultSet.getString(4);
            String date = resultSet.getString(6);
            String time = resultSet.getString(7);

            String output = "E-mail info: \n\t email_id: %d \n\t sender: %s" +
                    " \n\t receiver: %s \n\t subject: %s \n\t date: %s \n\t time: %s";

            System.out.println(String.format(output,emailID,sender,receiver,subject,date,time));
        }

    }

    public void addContact() {

        ContactList newContact = new ContactList();
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(dbURL, user, password)) {

            //SETTING CONTACT PERSON'S FULL NAME
            boolean matches = false;

            while (!matches) {
                System.out.println("Enter the contact person's full name: ");
                contactFullName = scanner.nextLine().toLowerCase().trim();
                newContact.setContactFullName(contactFullName);

                Pattern pattern = Pattern.compile("[a-zA-Z ]*");
                Matcher matcher = pattern.matcher(newContact.getContactFullName());
                matches = matcher.matches();
                if (matches) {
                    System.out.println("You have set contact person's full name successfully!");
                    System.out.println("______________________");
                } else {
                    System.out.println("Try again!");
                }
            }

            //SETTING CONTACT PERSON'S EMAIL ADDRESS
            boolean matches1 = false;

            while (!matches1) {
                System.out.println("Enter the contact person's e-mail address: ");
                contactEmail = scanner.nextLine();
                newContact.setContactEmail(contactEmail);

                Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
                Matcher matcher = pattern.matcher(newContact.getContactEmail());
                matches1 = matcher.matches();

                PreparedStatement prepareStatement = conn.prepareStatement("SELECT * FROM contacts WHERE email_address = ?;");
                prepareStatement.setString(1, newContact.getContactEmail());
                prepareStatement.executeQuery();

                ResultSet resultSet = prepareStatement.executeQuery();

                if (!resultSet.next()) {
                    System.out.println("You have set contact person's e-mail address successfully!");
                    System.out.println("______________________");
                } else {
                    System.out.println("This e-mail address already exists in your contact list!");
                    System.out.println(" ");
                }
            }

            insertContactData(conn, contactFullName, contactEmail);

        }catch (SQLException e){
            System.out.println("Something went wrong! " + e);
        }

    }

    public static void insertContactData(Connection conn, String contactFullName, String contactEmail) throws SQLException{

        String sql = "INSERT INTO contacts (full_name, email_address) VALUES (?, ?)";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, contactFullName);
        preparedStatement.setString(2, contactEmail);


        int rowInserted = preparedStatement.executeUpdate();

        if (rowInserted > 0) {
            System.out.println("This contact person has been added to your contacts' list successfully!");
            System.out.println("______________________");
        } else {
            System.out.println("Something went wrong!");
        }


    }

    public static void deleteContact(Connection conn) throws SQLException{

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter ID number of the contact person that you want to delete: ");
        int contactID = scanner.nextInt();

        String sql = "DELETE FROM contacts WHERE contact_id = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, contactID);

        int rowDeleted = preparedStatement.executeUpdate();

        if(rowDeleted > 0){
            System.out.println("The contact person was deleted successfully!");
        }else {
            System.out.println("Something went wrong!");
        }

    }

    public static void printContacts(Connection conn) throws SQLException{

        String sql = "SELECT * FROM contacts;";

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()){

            int contactID = resultSet.getInt(1);
            String contactFullName = resultSet.getString(2);
            String contactEmail = resultSet.getString(3);

            String output = "Your contacts' list: \n\t contact_id: %d \n\t full_name: %s \n\t email_address: %s";

            System.out.println(String.format(output,contactID,contactFullName,contactEmail));
        }

    }


}
