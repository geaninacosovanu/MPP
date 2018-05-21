package server;

import notification.Notification;
import notification.NotificationType;
import org.springframework.jms.core.JmsOperations;
import services.IInscriereNotificationService;

public class NotificationService implements IInscriereNotificationService {
    private JmsOperations jmsOperations;
    public NotificationService(JmsOperations operations) {
        jmsOperations=operations;
    }
    @Override
    public void inscriereAdded() {
        System.out.println("New inscriere notification");
        Notification notif=new Notification(NotificationType.NEW_INSCRIERE);
        jmsOperations.convertAndSend(notif);
        System.out.println("Sent message to ActiveMQ... " +notif);
    }
}
