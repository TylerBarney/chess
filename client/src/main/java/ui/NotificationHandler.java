package ui;

import model.webSocketMessages.ErrorMessage;
import model.webSocketMessages.LoadGameMessage;
import model.webSocketMessages.NotificationMessage;
import model.webSocketMessages.ServerMessage;

public class NotificationHandler {
    void notify(ServerMessage serverMessage){
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
            System.out.println(notificationMessage.message);
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
            ErrorMessage errorMessage = (ErrorMessage) serverMessage;
            System.out.println(errorMessage.getErrorMessage());
        }
    }
}
