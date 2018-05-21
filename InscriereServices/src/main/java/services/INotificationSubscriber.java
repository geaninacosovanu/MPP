package services;
import notification.Notification;

public interface INotificationSubscriber {
    void notificationReceived(Notification notification);
}
