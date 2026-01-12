package application;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_stages";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    // 1. Obtenir la connexion
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Charger le driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Établir la connexion
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion MySQL établie");
                
            } catch (ClassNotFoundException e) {
                System.out.println("❌ Driver MySQL non trouvé");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("❌ Erreur connexion MySQL: " + e.getMessage());
                System.out.println("👉 Vérifiez que:");
                System.out.println("   1. MySQL est démarré (XAMPP/WAMP)");
                System.out.println("   2. La base 'gestion_stages' existe");
                System.out.println("   3. Utilisateur: root, Mot de passe: (vide)");
            }
        }
        return connection;
    }
    
    // 2. Test de connexion simple
    public static void testConnexion() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Test connexion réussie");
            System.out.println("📊 Base de données: " + conn.getCatalog());
            conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Test connexion échoué: " + e.getMessage());
        }
    }
    
    // 3. Exécuter une requête SELECT (pour lire)
    public static ResultSet executeQuery(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("❌ Erreur requête: " + e.getMessage());
            return null;
        }
    }
    
    // 4. Exécuter INSERT/UPDATE/DELETE (pour modifier)
    public static int executeUpdate(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("❌ Erreur mise à jour: " + e.getMessage());
            return -1;
        }
    }
    
    // 5. Fermer la connexion
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion MySQL fermée");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}