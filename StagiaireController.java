package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StagiaireController {

    @FXML private TableView<Stagiaire> tableStagiaires;
    @FXML private TableColumn<Stagiaire, Integer> colId;
    @FXML private TableColumn<Stagiaire, String> colNom;
    @FXML private TableColumn<Stagiaire, String> colPrenom;
    @FXML private TableColumn<Stagiaire, String> colEmail;
    @FXML private TableColumn<Stagiaire, String> colFiliere;

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtFiliere;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiere"));

        chargerStagiaires();

        tableStagiaires.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> {
                    if (newSel != null) remplirFormulaire(newSel);
                }
        );
    }

    private void chargerStagiaires() {
        tableStagiaires.getItems().clear();
        try {
            ResultSet rs = DatabaseConnection.executeQuery(
                    "SELECT * FROM stagiaires ORDER BY nom"
            );
            while (rs != null && rs.next()) {
                Stagiaire s = new Stagiaire();
                s.setId(rs.getInt("id"));
                s.setNom(rs.getString("nom"));
                s.setPrenom(rs.getString("prenom"));
                s.setEmail(rs.getString("email"));
                s.setFiliere(rs.getString("filiere"));
                tableStagiaires.getItems().add(s);
            }
        } catch (SQLException e) {
            afficherErreur("Erreur", "Chargement échoué", e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        if (!champsValides()) return;

        String sql = String.format(
                "INSERT INTO stagiaires (nom, prenom, email, filiere) VALUES ('%s','%s','%s','%s')",
                txtNom.getText(), txtPrenom.getText(),
                txtEmail.getText(), txtFiliere.getText()
        );

        if (DatabaseConnection.executeUpdate(sql) > 0) {
            afficherSucces("Succès", "Ajout effectué", "Stagiaire ajouté avec succès.");
            viderFormulaire();
            chargerStagiaires();
        }
    }

    @FXML
    private void handleModifier() {
        Stagiaire s = tableStagiaires.getSelectionModel().getSelectedItem();
        if (s == null || !champsValides()) {
            afficherErreur("Erreur", "Sélection requise", "Sélectionnez un stagiaire.");
            return;
        }

        String sql = String.format(
                "UPDATE stagiaires SET nom='%s', prenom='%s', email='%s', filiere='%s' WHERE id=%d",
                txtNom.getText(), txtPrenom.getText(),
                txtEmail.getText(), txtFiliere.getText(),
                s.getId()
        );

        if (DatabaseConnection.executeUpdate(sql) > 0) {
            afficherSucces("Succès", "Modification réussie", "Données mises à jour.");
            chargerStagiaires();
        }
    }

    @FXML
    private void handleSupprimer() {
        Stagiaire s = tableStagiaires.getSelectionModel().getSelectedItem();
        if (s == null) {
            afficherErreur("Erreur", "Sélection requise", "Sélectionnez un stagiaire.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer stagiaire");
        confirm.setContentText("Supprimer " + s.getNom() + " " + s.getPrenom() + " ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            String sql = "DELETE FROM stagiaires WHERE id=" + s.getId();
            if (DatabaseConnection.executeUpdate(sql) > 0) {
                afficherSucces("Succès", "Suppression", "Stagiaire supprimé.");
                viderFormulaire();
                chargerStagiaires();
            }
        }
    }

    @FXML
    private void handleVider() {
        viderFormulaire();
        tableStagiaires.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleRetour() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            Stage stage = (Stage) tableStagiaires.getScene().getWindow();
            stage.setTitle("🎓 Gestion des Stages");
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            afficherErreur("Erreur", "Retour impossible", e.getMessage());
        }
    }

    private void remplirFormulaire(Stagiaire s) {
        txtNom.setText(s.getNom());
        txtPrenom.setText(s.getPrenom());
        txtEmail.setText(s.getEmail());
        txtFiliere.setText(s.getFiliere());
    }

    private void viderFormulaire() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        txtFiliere.clear();
    }

    private boolean champsValides() {
        if (txtNom.getText().isEmpty() ||
            txtPrenom.getText().isEmpty() ||
            txtEmail.getText().isEmpty() ||
            txtFiliere.getText().isEmpty()) {
            afficherErreur("Validation", "Champs manquants", "Veuillez remplir tous les champs.");
            return false;
        }
        return true;
    }

    private void afficherSucces(String titre, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void afficherErreur(String titre, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
