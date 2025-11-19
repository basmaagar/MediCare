import java.io.Serializable;
import java.time.LocalDate;

public abstract class Personne implements Serializable {
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;

    public Personne(String nom, String prenom, LocalDate dateNaissance) {
        if (dateNaissance.isAfter(LocalDate.now())) {
            throw new DateDeNaissanceFutureException("La date de naissance ne peut pas être dans le futur.");
        }
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
    }

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public LocalDate getDateNaissance() { return dateNaissance; }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + dateNaissance + ")";
    }
}