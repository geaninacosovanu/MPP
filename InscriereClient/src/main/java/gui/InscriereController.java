package gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Inscriere;
import model.Participant;
import model.Proba;
import model.User;
import repository.RepositoryException;
import services.IInscriereObserver;
import services.IInscriereService;
import services.InscriereServiceException;
import services.dto.ParticipantProbeDTO;
import services.dto.ProbaDTO;
import utils.ShowMessage;
import validator.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InscriereController implements IInscriereObserver {
    private IInscriereService service;
    private ObservableList<ProbaDTO> modelProba = FXCollections.observableArrayList();
    private User user;
    @FXML
    TableView<ProbaDTO> tableViewProba;
    @FXML
    TableColumn<Proba, String> tableColumnStil;
    @FXML
    TableColumn<Proba, Float> tableColumnDistanta;
    @FXML
    TableColumn<Proba, Integer> tableColumnNrParticipanti;

    @FXML
    TableView<ParticipantProbeDTO> tableViewParticipant;
    @FXML
    TableColumn<Participant, String> tableColumnNume;
    @FXML
    TableColumn<Participant, Integer> tableColumnVarsta;
    @FXML
    TableColumn<Participant, String> tableColumnProbe;
    @FXML
    ListView<Proba> listViewProbe;

    @FXML
    TextField textFieldNume;
    @FXML
    TextField textFieldVarsta;
    @FXML
    Button buttonInscriere;

    @FXML
    CheckBox checkBoxParticipantExistent;

    public void setService(IInscriereService service,User user) {
        this.user=user;
        this.service = service;
        try {
            modelProba = FXCollections.observableArrayList(service.getAllProba());
        } catch (InscriereServiceException e) {
            e.printStackTrace();
        }
        tableViewProba.setItems(modelProba);
        tableViewProba.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    tableViewParticipant.setItems(FXCollections.observableArrayList(service.getParticipanti(newSelection.getIdProba())));
                } catch (InscriereServiceException e) {
                    e.printStackTrace();
                }
            }
        });
        List<Proba> probe = new ArrayList<>();
        for (ProbaDTO p : modelProba)
            probe.add(p.getProba());
        listViewProbe.setItems(FXCollections.observableArrayList(probe));
        listViewProbe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void initialize() {
        initializeTableProba();
        initializeTableParticipanti();

    }

    private void initializeTableParticipanti() {

        tableColumnNume.setCellValueFactory(new PropertyValueFactory<>("participantNume"));
        tableColumnVarsta.setCellValueFactory(new PropertyValueFactory<>("participantVarsta"));
        tableColumnProbe.setCellValueFactory(new PropertyValueFactory<>("probe"));


    }

    private void initializeTableProba() {
        tableColumnStil.setCellValueFactory(new PropertyValueFactory<>("numeProba"));
        tableColumnDistanta.setCellValueFactory(new PropertyValueFactory<>("distantaProba"));

        tableColumnNrParticipanti.setCellValueFactory(new PropertyValueFactory<>("nrParticipanti"));
    }

    public void handleLogoutBotton(MouseEvent mouseEvent) {
        try {
            service.logout(user,this);
            showLoginWindow(initLoginView());
            ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
        } catch (InscriereServiceException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
        }

    }

    public BorderPane initLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/LoginView.fxml"));
            BorderPane root = loader.load();
            LoginController ctr = loader.getController();
            ctr.setService(service);
            return root;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void showLoginWindow(BorderPane root) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Login");
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    public void handleButtonInscriere(MouseEvent mouseEvent) {
        String nume = textFieldNume.getText();
        Integer varsta = Integer.parseInt(textFieldVarsta.getText());
        List<Proba> probe = new ArrayList<>(listViewProbe.getSelectionModel().getSelectedItems());
        try {
            if (probe.size() == 0)
                ShowMessage.showMessage(Alert.AlertType.WARNING, "Warning", "Nu ati selectat nicio proba!");
            else if (checkBoxParticipantExistent.isSelected() == false) {
                service.saveInscriere(nume, varsta, probe, false);
            } else {
                service.saveInscriere(nume, varsta, probe, true);
            }
            String msg = "Participantul " + nume + " a fost inscris la probele:\n";
            for (Proba p : probe)
                msg += p.toString() + "\n";
            ShowMessage.showMessage(Alert.AlertType.CONFIRMATION, "Confirmation", msg);
            textFieldNume.clear();
            textFieldVarsta.clear();
            listViewProbe.getSelectionModel().clearSelection();
            checkBoxParticipantExistent.setSelected(false);
        } catch (RepositoryException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        } catch (ValidationException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());
        } catch (InscriereServiceException e) {
            ShowMessage.showMessage(Alert.AlertType.ERROR, "Eroare", e.getMessage());

        }
    }


    @Override
    public void inscriereAdded() {
        Platform.runLater(() -> {
            try {
                modelProba.setAll(service.getAllProba());
            } catch (InscriereServiceException e) {
                e.printStackTrace();
            }
        });
    }
}