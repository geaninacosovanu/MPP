package gui.ams;

import notification.Notification;
import org.springframework.jms.core.JmsOperations;
import services.INotificationReceiver;
import services.INotificationSubscriber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NotificationReceiver implements INotificationReceiver{
    private JmsOperations jmsOperations;
    private boolean running;
    public NotificationReceiver(JmsOperations operations) {
        jmsOperations=operations;
    }
    ExecutorService service;
    INotificationSubscriber subscriber;

    private void run(){
        while(running){
            Notification notif=(Notification)jmsOperations.receiveAndConvert();
            System.out.println("Received Notification... "+notif);
            subscriber.notificationReceived(notif);

        }
    }
    @Override
    public void start(INotificationSubscriber subscriber) {
        System.out.println("Starting notification receiver ...");
        running=true;
        this.subscriber=subscriber;
        service = Executors.newSingleThreadExecutor();
        service.submit(()->{run();});
    }

    @Override
    public void stop() {
        running=false;
        try {
            service.awaitTermination(100, TimeUnit.MILLISECONDS);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped notification receiver");
    }
}
