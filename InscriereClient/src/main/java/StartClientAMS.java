import gui.LoginController;
import gui.ams.LoginControllerAMS;
import gui.ams.NotificationReceiver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import network.protocol.InscriereServerProxy;
import network.protocol.ams.InscriereServerAMSProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.IInscriereService;
import services.IInscriereServiceAMS;
import services.InscriereServiceException;
import utils.ShowMessage;

import java.io.IOException;
import java.util.Properties;

public class StartClientAMS  extends Application {
    @Override
    public void start(Stage primaryStage)  {
        System.out.println("In start");

        primaryStage.setTitle("Login");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginView.fxml"));
        BorderPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LoginControllerAMS ctr = loader.getController();

        IInscriereServiceAMS service= null;
        NotificationReceiver receiver=null;
       // try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-client");
            service = context.getBean("inscriereService",InscriereServerAMSProxy.class);
            receiver= context.getBean("notificationReceiver",NotificationReceiver.class);
            ctr.setService(service);
            ctr.setReceiver(receiver);
            Scene scene = new Scene(root);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
//        } catch (InscriereServiceException e) {
//            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare","Server indisponibil");
//        }

    }




    public static void main(String[] args) {
        launch(args);
    }
}
