import gui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import network.protobuffProtocol.ProtoInscriereProxy;
import network.protocol.InscriereServerProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.IInscriereService;
import services.InscriereServiceException;
import utils.ShowMessage;

import javax.sound.midi.ShortMessage;
import java.io.IOException;
import java.util.Properties;

public class StartClient  extends Application {

    @Override
    public void start(Stage primaryStage)  {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
        } catch (IOException e) {
            System.err.println("Eroare in fisierul de properietati " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host");
        int serverPort = 5000;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Port gresit " + e.getMessage());
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        primaryStage.setTitle("Login");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginView.fxml"));
        BorderPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginController ctr = loader.getController();

        IInscriereService service= null;
        try {
            service = new ProtoInscriereProxy(serverIP,serverPort);
            ctr.setService(service);
            Scene scene = new Scene(root);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (InscriereServiceException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare","Server indisponibil");
        }

    }




    public static void main(String[] args) {
        launch(args);
    }
}
