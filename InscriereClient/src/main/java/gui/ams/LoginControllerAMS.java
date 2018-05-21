package gui.ams;

import gui.InscriereController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import services.IInscriereService;
import services.IInscriereServiceAMS;
import services.InscriereServiceException;
import utils.ShowMessage;

import java.io.IOException;

public class LoginControllerAMS {
    private IInscriereServiceAMS inscriereService;
    private ObservableList<User> model;
    private NotificationReceiver receiver;


    @FXML
    TextField textFieldUsername;
    @FXML
    PasswordField passwordFieldParola;
    @FXML
    Button buttonLogin;



    public void setService(IInscriereServiceAMS inscriereService) {
        this.inscriereService = inscriereService;
    }

    public void setReceiver(NotificationReceiver receiver) {
        this.receiver = receiver;
    }

    public void initialize(){}

    public void handleLoginBotton(MouseEvent mouseEvent) {
        String userName = textFieldUsername.getText();
        String parola = passwordFieldParola.getText();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/InscriereView.fxml"));
            BorderPane root=loader.load();
            InscriereControllerAMS ctr = loader.getController();
            ctr.setService(inscriereService,new User(userName,parola));

            if(inscriereService.login(userName,parola)){
                ctr.setReceiver(this.receiver);
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Inscriere");
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.setResizable(false);
                dialogStage.show();
                ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();
            }
            else
                ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare","Username sau parola invalida!");
        } catch (InscriereServiceException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());

        } catch (IOException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());

        }
    }
}
