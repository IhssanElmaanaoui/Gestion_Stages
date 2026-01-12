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

public class EntrepriseController {

    @FXML private TableView<Entreprise> tableEntreprises;
    @FXML private TableColumn<Entreprise, Integer> colId;
    @FXML private TableColumn<Entreprise, String> colNom;
    @FXML private TableColumn<Entreprise, String> colAdresse;
    @FXML private TableColumn<Entreprise, String> colTelephone;

    @FXML private TextField txtNom;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtTelephone;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        chargerEntreprises();

        tableEntreprises.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> {
                    if (newSel != null) remplirFormulaire(newSel);
                }
        );
    }

    private void chargerEntreprises() {
        tableEntreprises.getItems().clear();
        try {
            ResultSet rs = DatabaseConnection.executeQuery(
                    "SELECT * FROM entreprises ORDER BY nom"
            );
            while (rs != null && rs.next()) {
                Entreprise e = new Entreprise();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setAdresse(rs.getString("adresse"));
                e.setTelephone(rs.getString("telephone"));
                tableEntreprises.getItems().add(e);
            }
        } catch (SQLException e) {
            afficherErreur("Erreur", "Chargement échoué", e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        if (!champsValides()) return;

        String sql = String.format(
                "INSERT INTO entreprises (nom, adresse, telephone) VALUES ('%s','%s','%s')",
                txtNom.getText(), txtAdresse.getText(), txtTelephone.getText()
        );

        if (DatabaseConnection.executeUpdate(sql) > 0) {
            afficherSucces("Succès", "Entreprise ajoutée", "Ajout effectué avec succès.");
            viderFormulaire();
            chargerEntreprises();
        }
    }

    @FXML
    private void handleModifier() {
        Entreprise e = tableEntreprises.getSelectionModel().getSelectedItem();
        if (e == null || !champsValides()) {
            afficherErreur("Erreur", "Sélection requise", "Sélectionnez une entreprise.");
            return;
        }

        String sql = String.format(
                "UPDATE entreprises SET nom='%s', adresse='%s', telephone='%s' WHERE id=%d",
                txtNom.getText(), txtAdresse.getText(), txtTelephone.getText(),
                e.getId()
        );

        if (DatabaseConnection.executeUpdate(sql) > 0) {
            afficherSucces("Succès", "Modification réussie", "Entreprise modifiée.");
            chargerEntreprises();
        }
    }

    @FXML
    private void handleSupprimer() {
        Entreprise e = tableEntreprises.getSelectionModel().getSelectedItem();
        if (e == null) {
            afficherErreur("Erreur", "Sélection requise", "Sélectionnez une entreprise.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer entreprise");
        confirm.setContentText("Supprimer " + e.getNom() + " ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            String sql = "DELETE FROM entreprises WHERE id=" + e.getId();
            if (DatabaseConnection.executeUpdate(sql) > 0) {
                afficherSucces("Succès", "Suppression", "Entreprise supprimée.");
                viderFormulaire();
                chargerEntreprises();
            }
        }
    }

    @FXML
    private void handleVider() {
        viderFormulaire();
        tableEntreprises.getSelectionModel().clearSelection();
    }

    // 🔙 BOUTON RETOUR
    @FXML
    private void handleRetour() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            Stage stage = (Stage) tableEntreprises.getScene().getWindow();
            stage.setTitle("🎓 Gestion des Stages");
            stage.setScene(new Scene(root, 900, 600));
        } catch (IOException e) {
            afficherErreur("Erreur", "Retour impossible", e.getMessage());
        }
    }

    private void remplirFormulaire(Entreprise e) {
        txtNom.setText(e.getNom());
        txtAdresse.setText(e.getAdresse());
        txtTelephone.setText(e.getTelephone());
    }

    private void viderFormulaire() {
        txtNom.clear();
        txtAdresse.clear();
        txtTelephone.clear();
    }

    private boolean champsValides() {
        if (txtNom.getText().isEmpty()) {
            afficherErreur("Validation", "Champ manquant", "Le nom est obligatoire.");
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
