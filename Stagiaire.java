package application;

public class Stagiaire {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String filiere;
    
    // Constructeur par défaut
    public Stagiaire() {
    }
    
    // Constructeur avec paramètres
    public Stagiaire(String nom, String prenom, String email, String filiere) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.filiere = filiere;
    }
    
    // Constructeur complet
    public Stagiaire(int id, String nom, String prenom, String email, String filiere) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.filiere = filiere;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFiliere() {
        return filiere;
    }
    
    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }
    
    // Pour afficher dans les listes
    @Override
    public String toString() {
        return nom + " " + prenom + " - " + filiere;
    }
    
    // Pour le TableView
    public String getNomComplet() {
        return nom + " " + prenom;
    }
}