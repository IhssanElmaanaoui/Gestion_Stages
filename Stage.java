package application;

import java.time.LocalDate;

public class Stage {
    private int id;
    private String sujet;
    private String encadrant;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int stagiaireId;
    private int entrepriseId;
    
    // Pour affichage (noms au lieu des IDs)
    private String stagiaireNom;
    private String entrepriseNom;
    
    // Constructeurs
    public Stage() {
    }
    
    public Stage(String sujet, String encadrant, LocalDate dateDebut, LocalDate dateFin, 
                 int stagiaireId, int entrepriseId) {
        this.sujet = sujet;
        this.encadrant = encadrant;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.stagiaireId = stagiaireId;
        this.entrepriseId = entrepriseId;
    }
    
    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getSujet() {
        return sujet;
    }
    
    public void setSujet(String sujet) {
        this.sujet = sujet;
    }
    
    public String getEncadrant() {
        return encadrant;
    }
    
    public void setEncadrant(String encadrant) {
        this.encadrant = encadrant;
    }
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public int getStagiaireId() {
        return stagiaireId;
    }
    
    public void setStagiaireId(int stagiaireId) {
        this.stagiaireId = stagiaireId;
    }
    
    public int getEntrepriseId() {
        return entrepriseId;
    }
    
    public void setEntrepriseId(int entrepriseId) {
        this.entrepriseId = entrepriseId;
    }
    
    public String getStagiaireNom() {
        return stagiaireNom;
    }
    
    public void setStagiaireNom(String stagiaireNom) {
        this.stagiaireNom = stagiaireNom;
    }
    
    public String getEntrepriseNom() {
        return entrepriseNom;
    }
    
    public void setEntrepriseNom(String entrepriseNom) {
        this.entrepriseNom = entrepriseNom;
    }
    
    // Pour l'affichage
    @Override
    public String toString() {
        return sujet + " (Encadrant: " + encadrant + ")";
    }
    
    

    
    // Méthode utile pour TableView
    public String getDuree() {
        if (dateDebut != null && dateFin != null) {
            return dateDebut + " à " + dateFin;
        }
        return "Non défini";
    }
}