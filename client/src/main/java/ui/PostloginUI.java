package ui;

import chess.ChessGame;
import model.webSocketMessages.NotificationMessage;

import java.util.Scanner;

public class PostloginUI implements NotificationHandler {
    ServerFacade facade;
    boolean calledList = false;
    public PostloginUI(ServerFacade facade) throws Exception {
        this.facade = facade;
        System.out.println("[LOGGED_IN] >>> Type HELP to get started.");
        while(true){
            try{
                System.out.print("[LOGGED_IN] >>> ");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                var input = line.split(" ");
                if (input[0].equalsIgnoreCase("help")){
                    help();
                } else if (input[0].equalsIgnoreCase("Logout")){
                    facade.logout();
                    System.out.println("You are logged out");
                    break;
                } else if (input[0].equalsIgnoreCase("create")){
                    if (input.length < 2) {System.out.println("Not enough params"); continue;}
                    String gameID = facade.createGame(input[1]);
                    System.out.println("You created a game with ID: " + gameID);
                } else if (input[0].equalsIgnoreCase("list")){
                    calledList = true;
                    System.out.println(facade.listGames().toString());
                } else if (input[0].equalsIgnoreCase("join")){
                    if (!calledList) {System.out.println("Call list first"); continue;}
                    if (input.length < 3) {System.out.println("Not enough params"); continue;}
                    if (Integer.parseInt(input[1]) < 0 || Integer.parseInt(input[1]) >= facade.gameList.games.length){
                        System.out.println("Game ID invalid");
                    } else {
                        ChessGame game = facade.joinGame(input[2], Integer.parseInt(input[1]));
                        System.out.println("You have joined gameID: " + input[1] + " as " + input[2] + "player");
                        new GameplayUI(facade, game, input[1]);
                    }
                } else if (input[0].equalsIgnoreCase("observe")){
                    if (!calledList) {System.out.println("Call list first"); continue;}
                    if (input.length < 2) {System.out.println("Not enough params"); continue;}
                    if (Integer.parseInt(input[1]) < 0 || Integer.parseInt(input[1]) >= facade.gameList.games.length){
                        System.out.println("Game ID invalid");
                    } else {
                        ChessGame game = facade.observeGame(Integer.parseInt(input[1]));
                        System.out.println("You are observing gameID: " + input[1]);
                        new GameplayUI(facade, game, "white");
                    }

                } else {
                    System.out.println("Invalid command. Type HELP for valid commands");
                }
            }  catch (Exception ex){
                String[] exMessage = ex.getMessage().split(" ");
                int error = Integer.parseInt(exMessage[5]);
                handleErrors(error);
            }
        }
    }

    void help() {
        System.out.println("create <NAME> - a game");
        System.out.println("list - games");
        System.out.println("join <ID> [WHITE|BLACK] - a game");
        System.out.println("observe <ID> - a game");
        System.out.println("logout - when you are done");
        System.out.println("help - with possible commands");
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
