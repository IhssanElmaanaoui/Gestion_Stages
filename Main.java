package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Test connexion MySQL au démarrage
            testConnexion();
            
            System.out.println("=== Chargement depuis même package ===");
            
            // Charger le FXML depuis le même package
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            
            // Créer la scène
            Scene scene = new Scene(root, 1000, 700);
            
            // Appliquer le style CSS (même package)
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            
            // Configurer la fenêtre
            primaryStage.setTitle("🎓 Gestion des Stages - Ihssan Elmaanaoui & Yousra Essebbane");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            System.out.println("✅ Application démarrée avec succès !");
            
        } catch (Exception e) {
            System.out.println("❌ Erreur au démarrage: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void testConnexion() {
        try {
            // Test driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL chargé");
            
            // Test connexion à la base
            DatabaseConnection.testConnexion();
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver MySQL non trouvé");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}