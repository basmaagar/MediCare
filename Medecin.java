import java.time.LocalDate;

public class Medecin extends Employe {
    private String specialite;
    private String numOrdre;

    public Medecin(String nom, String prenom, LocalDate dateNaissance, String idEmploye, String specialite, String numOrdre) {
        super(nom, prenom, dateNaissance, idEmploye, Role.MEDECIN);
        this.specialite = specialite;
        this.numOrdre = numOrdre;
    }


    public String getSpecialite() { return specialite; }
    public String getNumOrdre() { return numOrdre; }

    @Override
    public String afficherDetailsPoste() {
        return "Spécialité: " + specialite + ", N° Ordre: " + numOrdre;
    }

    @Override
    public String toString() {
        return "Dr. " + super.toString() + ", " + afficherDetailsPoste();
    }
}