package ui;

import model.webSocketMessages.NotificationMessage;

public interface NotificationHandler {
    void notify(NotificationMessage notificationMessage);
}
