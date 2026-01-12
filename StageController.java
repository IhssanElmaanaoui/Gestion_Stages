package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@SuppressWarnings("unused")
public class StageController {
    
    @FXML private TableView<Stage> tableStages;
    @FXML private TableColumn<Stage, Integer> colId;
    @FXML private TableColumn<Stage, String> colSujet;
    @FXML private TableColumn<Stage, String> colEncadrant;
    @FXML private TableColumn<Stage, String> colDateDebut;
    @FXML private TableColumn<Stage, String> colDateFin;
    @FXML private TableColumn<Stage, String> colStagiaire;
    @FXML private TableColumn<Stage, String> colEntreprise;
    
    @FXML private TextField txtSujet, txtEncadrant;
    @FXML private DatePicker dateDebut, dateFin;
    @FXML private ComboBox<Stagiaire> comboStagiaire;
    @FXML private ComboBox<Entreprise> comboEntreprise;
    
    @FXML
    public void initialize() {
        configurerTable();
        chargerCombos();
        chargerStages();
        configurerSelection();
    }
    
    private void configurerTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSujet.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        colEncadrant.setCellValueFactory(new PropertyValueFactory<>("encadrant"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colStagiaire.setCellValueFactory(new PropertyValueFactory<>("stagiaireNom"));
        colEntreprise.setCellValueFactory(new PropertyValueFactory<>("entrepriseNom"));
    }
    
    private void chargerCombos() {
        // Charger stagiaires dans la combo
        try {
            ResultSet rsStagiaires = DatabaseConnection.executeQuery("SELECT * FROM stagiaires ORDER BY nom");
            while (rsStagiaires != null && rsStagiaires.next()) {
                Stagiaire s = new Stagiaire(
                    rsStagiaires.getInt("id"),
                    rsStagiaires.getString("nom"),
                    rsStagiaires.getString("prenom"),
                    rsStagiaires.getString("email"),
                    rsStagiaires.getString("filiere")
                );
                comboStagiaire.getItems().add(s);
            }
        } catch (SQLException e) {
            afficherErreur("Erreur", "Impossible de charger les stagiaires", e.getMessage());
        }
        
        // Charger entreprises dans la combo
        try {
            ResultSet rsEntreprises = DatabaseConnection.executeQuery("SELECT * FROM entreprises ORDER BY nom");
            while (rsEntreprises != null && rsEntreprises.next()) {
                Entreprise e = new Entreprise(
                    rsEntreprises.getInt("id"),
                    rsEntreprises.getString("nom"),
                    rsEntreprises.getString("adresse"),
                    rsEntreprises.getString("telephone")
                );
                comboEntreprise.getItems().add(e);
            }
        } catch (SQLException e) {
            afficherErreur("Erreur", "Impossible de charger les entreprises", e.getMessage());
        }
    }
    
    private void chargerStages() {
        tableStages.getItems().clear();
        try {
            String sql = "SELECT s.*, st.nom as stagiaire_nom, st.prenom as stagiaire_prenom, " +
                        "e.nom as entreprise_nom FROM stages s " +
                        "LEFT JOIN stagiaires st ON s.stagiaire_id = st.id " +
                        "LEFT JOIN entreprises e ON s.entreprise_id = e.id " +
                        "ORDER BY s.date_debut DESC";
            
            ResultSet rs = DatabaseConnection.executeQuery(sql);
            while (rs != null && rs.next()) {
                Stage stage = new Stage();
                stage.setId(rs.getInt("id"));
                stage.setSujet(rs.getString("sujet"));
                stage.setEncadrant(rs.getString("encadrant"));
                stage.setDateDebut(rs.getDate("date_debut").toLocalDate());
                stage.setDateFin(rs.getDate("date_fin").toLocalDate());
                stage.setStagiaireId(rs.getInt("stagiaire_id"));
                stage.setEntrepriseId(rs.getInt("entreprise_id"));
                stage.setStagiaireNom(rs.getString("stagiaire_nom") + " " + rs.getString("stagiaire_prenom"));
                stage.setEntrepriseNom(rs.getString("entreprise_nom"));
                
                tableStages.getItems().add(stage);
            }
        } catch (SQLException e) {
            afficherErreur("Erreur", "Impossible de charger les stages", e.getMessage());
        }
    }
    
    @SuppressWarnings("unused")
	private void configurerSelection() {
        tableStages.getSelectionModel().selectedItemProperty().addListener(
            (observable, ancienneSelection, nouvelleSelection) -> {
                if (nouvelleSelection != null) {
                    remplirFormulaire(nouvelleSelection);
                }
            });
    }
    
    // Ajouter un stage
    @FXML
    private void handleAjouter() {
        if (champsValides()) {
            String sql = String.format(
                "INSERT INTO stages (sujet, encadrant, date_debut, date_fin, stagiaire_id, entreprise_id) " +
                "VALUES ('%s', '%s', '%s', '%s', %d, %d)",
                txtSujet.getText(), txtEncadrant.getText(),
                dateDebut.getValue(), dateFin.getValue(),
                comboStagiaire.getValue().getId(),
                comboEntreprise.getValue().getId()
            );
            
            int result = DatabaseConnection.executeUpdate(sql);
            if (result > 0) {
                afficherSucces("Succès", "Stage ajouté", "Le stage a été ajouté avec succès.");
                viderFormulaire();
                chargerStages();
            }
        }
    }
    
    // Modifier un stage
    @FXML
    private void handleModifier() {
        Stage selected = tableStages.getSelectionModel().getSelectedItem();
        if (selected != null && champsValides()) {
            String sql = String.format(
                "UPDATE stages SET sujet='%s', encadrant='%s', date_debut='%s', date_fin='%s', " +
                "stagiaire_id=%d, entreprise_id=%d WHERE id=%d",
                txtSujet.getText(), txtEncadrant.getText(),
                dateDebut.getValue(), dateFin.getValue(),
                comboStagiaire.getValue().getId(),
                comboEntreprise.getValue().getId(),
                selected.getId()
            );
            
            int result = DatabaseConnection.executeUpdate(sql);
            if (result > 0) {
                afficherSucces("Succès", "Stage modifié", "Les modifications ont été enregistrées.");
                chargerStages();
            }
        } else {
            afficherErreur("Erreur", "Aucun stage sélectionné", "Veuillez sélectionner un stage à modifier.");
        }
    }
    
    // Supprimer un stage
    @FXML
    private void handleSupprimer() {
        Stage selected = tableStages.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer stage");
            confirm.setContentText("Êtes-vous sûr de vouloir supprimer le stage : " + selected.getSujet() + " ?");
            
            if (confirm.showAndWait().get() == ButtonType.OK) {
                String sql = "DELETE FROM stages WHERE id=" + selected.getId();
                int result = DatabaseConnection.executeUpdate(sql);
                if (result > 0) {
                    afficherSucces("Succès", "Stage supprimé", "Le stage a été supprimé.");
                    viderFormulaire();
                    chargerStages();
                }
            }
        } else {
            afficherErreur("Erreur", "Aucun stage sélectionné", "Veuillez sélectionner un stage à supprimer.");
        }
    }
    
    // Vider le formulaire
    @FXML
    private void handleVider() {
        viderFormulaire();
        tableStages.getSelectionModel().clearSelection();
    }
    
    // Remplir le formulaire
    private void remplirFormulaire(Stage stage) {
        txtSujet.setText(stage.getSujet());
        txtEncadrant.setText(stage.getEncadrant());
        dateDebut.setValue(stage.getDateDebut());
        dateFin.setValue(stage.getDateFin());
        
        // Trouver et sélectionner le stagiaire dans la combo
        for (Stagiaire s : comboStagiaire.getItems()) {
            if (s.getId() == stage.getStagiaireId()) {
                comboStagiaire.setValue(s);
                break;
            }
        }
        
        // Trouver et sélectionner l'entreprise dans la combo
        for (Entreprise e : comboEntreprise.getItems()) {
            if (e.getId() == stage.getEntrepriseId()) {
                comboEntreprise.setValue(e);
                break;
            }
        }
    }
    
    // Vider tous les champs
    private void viderFormulaire() {
        txtSujet.clear();
        txtEncadrant.clear();
        dateDebut.setValue(null);
        dateFin.setValue(null);
        comboStagiaire.setValue(null);
        comboEntreprise.setValue(null);
    }
    
    @FXML
    private void handleRetour() {
        try {
            // Charger le FXML de la page principale
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            
            // Récupérer la scène actuelle et mettre à jour le contenu
            tableStages.getScene().setRoot(root);
        } catch (IOException e) {
            afficherErreur("Erreur", "Impossible de revenir à l'accueil", e.getMessage());
        }
    }

    
    // Validation des champs
    private boolean champsValides() {
        if (txtSujet.getText().isEmpty()) {
            afficherErreur("Validation", "Champ manquant", "Le sujet du stage est obligatoire.");
            return false;
        }
        if (dateDebut.getValue() == null || dateFin.getValue() == null) {
            afficherErreur("Validation", "Dates manquantes", "Veuillez saisir les dates de début et fin.");
            return false;
        }
        if (dateDebut.getValue().isAfter(dateFin.getValue())) {
            afficherErreur("Validation", "Dates invalides", "La date de début doit être avant la date de fin.");
            return false;
        }
        if (comboStagiaire.getValue() == null) {
            afficherErreur("Validation", "Stagiaire manquant", "Veuillez sélectionner un stagiaire.");
            return false;
        }
        if (comboEntreprise.getValue() == null) {
            afficherErreur("Validation", "Entreprise manquante", "Veuillez sélectionner une entreprise.");
            return false;
        }
        return true;
    }
    
    // Messages
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