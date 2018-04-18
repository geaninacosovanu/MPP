package gui;

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
import services.InscriereServiceException;
import utils.ShowMessage;

import java.io.IOException;


public class LoginController {
    private IInscriereService inscriereService;
    private ObservableList<User> model;


    @FXML
    TextField textFieldUsername;
    @FXML
    PasswordField passwordFieldParola;
    @FXML
    Button buttonLogin;



    public void setService(IInscriereService inscriereService) {
        this.inscriereService = inscriereService;
    }
    public void initialize(){}

    public void handleLoginBotton(MouseEvent mouseEvent) {
        String userName = textFieldUsername.getText();
        String parola = passwordFieldParola.getText();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/InscriereView.fxml"));
            BorderPane root=loader.load();
            InscriereController ctr = loader.getController();
            ctr.setService(inscriereService,new User(userName,parola));

            if(inscriereService.login(userName,parola,ctr)){
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