package notification;

public class Notification {
    private NotificationType type;
    private String user;


    public Notification() {
    }

    public Notification(NotificationType type) {

        this.type = type;
    }

    public Notification(NotificationType type, String user) {
        this.type = type;
        this.user = user;
    }


    public NotificationType getType() {
        return type;
    }
    public void setType(NotificationType type) {
        this.type = type;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Notification{ " +
                "type=" + type +
                ", user=" + user + '}';
    }
}
