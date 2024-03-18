package ui;

import model.AuthData;

import java.util.Scanner;

public class PostloginUI {
    ServerFacade facade;
    public PostloginUI(ServerFacade facade) throws Exception {
        this.facade = facade;
        System.out.println("[LOGGED_IN] >>> Type HELP to get started.");
        while(true){
            System.out.print("[LOGGED_IN] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var input = line.split(" ");
            for (var string : input) {
                if (string.equalsIgnoreCase("help")){
                    help();
                } else if (string.equalsIgnoreCase("register")){
                    AuthData response = facade.register(input[1], input[2], input[3]);
                    System.out.println("You are logged in as: " + response.getUserName());
                } else if (string.equalsIgnoreCase("login")){
                    AuthData response = facade.login(input[1], input[2]);
                    System.out.println("You are logged in as: " + response.getUserName());
                } else {
                    System.out.println("Invalid command. Type HELP for valid commands");
                }
            }
        }
    }

    void help() {
        System.out.println("create <NAME> - a game");
        System.out.println("list - games");
        System.out.println("join <ID> [WHITE|BLACK|<empty>] - a game");
        System.out.println("observe <ID> - a game");
        System.out.println("logout - when you are done");
        System.out.println("help - with possible commands");
    }
}
