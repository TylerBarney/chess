package ui;

import model.AuthData;
import model.webSocketMessages.NotificationMessage;

import java.net.ProtocolException;
import java.util.Scanner;

public class PreloginUI implements NotificationHandler{
    ServerFacade facade = new ServerFacade(8080, this);
    public PreloginUI() throws Exception {
        System.out.println("Welcome to chess. Type HELP to get started.");
        while(true){
            try{
                System.out.print("[LOGGED_OUT] >>> ");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                var input = line.split(" ");
                if (input[0].equalsIgnoreCase("quit")){
                    quit();
                } else if (input[0].equalsIgnoreCase("help")){
                    help();
                } else if (input[0].equalsIgnoreCase("register")){
                    if (input.length < 4) {System.out.println("Not enough params"); continue;}
                    AuthData response = facade.register(input[1], input[2], input[3]);
                    System.out.println("You are logged in as: " + response.getUserName());
                    new PostloginUI(facade);
                } else if (input[0].equalsIgnoreCase("login")){
                    if (input.length < 3) {System.out.println("Not enough params"); continue;}
                    AuthData response = facade.login(input[1], input[2]);
                    System.out.println("You are logged in as: " + response.getUserName());
                    new PostloginUI(facade);
                } else {
                    System.out.println("Invalid command. Type HELP for valid commands");
                }
            }catch (Exception ex){
                System.out.println(ex.getMessage());
                String[] exMessage = ex.getMessage().split(" ");
                int error = Integer.parseInt(exMessage[5]);
                handleErrors(error);
            }

        }
    }

    void help() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    void quit() {
        System.exit(0);
    }
    void handleErrors(int errorCode){
        if (errorCode == 401){
            System.out.println("Error: unauthorized");
        }  else if (errorCode == 403){
            System.out.println("Error: already taken");
        } else {
            System.out.println("Error: bad request");
        }
    }


    @Override
    public void notify(NotificationMessage notificationMessage) {
        System.out.println(notificationMessage.message);
    }
}
