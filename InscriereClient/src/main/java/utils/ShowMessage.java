package utils;
import javafx.scene.control.Alert;

public class ShowMessage {
    public ShowMessage() {
    }
    public static void showMessage(Alert.AlertType type, String nume, String msg){
        Alert message = new Alert(type);
        message.setTitle(nume);
        message.setContentText(msg);
        message.showAndWait();
    }
}
