package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void handleStagiaires() {
        changerScene("stagiaires.fxml", "📋 Gestion des Stagiaires");
    }

    @FXML
    private void handleEntreprises() {
        changerScene("entreprises.fxml", "🏢 Gestion des Entreprises");
    }

    @FXML
    private void handleStages() {
        changerScene("stages.fxml", "📅 Gestion des Stages");
    }

    @FXML
    private void handleAPropos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Gestion des Stages");
        alert.setContentText(
                "Développé par :\n" +
                "• Ihssan Elmaanaoui\n" +
                "• Yousra Essebbane\n\n" +
                "Encadrant : M. Mazoul"
        );
        alert.showAndWait();
    }

    @FXML
    private void handleQuitter() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Quitter l'application");
        confirm.setContentText("Êtes-vous sûr de vouloir quitter ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            DatabaseConnection.closeConnection();
            System.exit(0);
        }
    }

    // 🔁 Navigation dans la MÊME fenêtre
    private void changerScene(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

            Stage stage = (Stage) Stage.getWindows()
                    .filtered(window -> window.isShowing())
                    .get(0);

            stage.setTitle(title);
            stage.setScene(new Scene(root, 900, 600));

        } catch (IOException e) {
            afficherErreur(
                    "Erreur",
                    "Impossible d'ouvrir " + title,
                    e.getMessage()
            );
        }
    }

    private void afficherErreur(String titre, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
