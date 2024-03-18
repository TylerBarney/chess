package ui;

import java.net.ProtocolException;
import java.util.Scanner;

public class PreloginUI {
    ServerFacade facade;
    public PreloginUI(ServerFacade facade) throws Exception {
        this.facade = facade;
        System.out.println("Welcome to chess. Type HELP to get started.");
        while(true){
            System.out.print("[LOGGED_OUT] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var input = line.split(" ");
            for (var string : input) {
                if (string.equalsIgnoreCase("quit")){
                    quit();
                } else if (string.equalsIgnoreCase("help")){
                    help();
                } else if (string.equalsIgnoreCase("register")){
                    facade.register(input[1], input[2], input[3]);
                } else if (string.equalsIgnoreCase("login")){
                    login(input[1], input[2]);
                } else {
                    System.out.println("Invalid command. Type HELP for valid commands");
                }
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

    void register(String username, String password, String email) {

    }

    void login(String username, String password) {

    }

}
