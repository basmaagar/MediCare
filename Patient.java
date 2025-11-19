import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient extends Personne {
    private String numDossier;
    private List<String> historiqueMedical;

    public Patient(String nom, String prenom, LocalDate dateNaissance, String numDossier) {
        super(nom, prenom, dateNaissance);
        this.numDossier = numDossier;
        this.historiqueMedical = new ArrayList<>();
    }

    public String getNumDossier() { return numDossier; }
    public List<String> getHistoriqueMedical() { return historiqueMedical; }

    public void ajouterAHistorique(String note) {
        this.historiqueMedical.add(note);
    }

    @Override
    public String toString() {
        return "Patient " + super.toString() + ", Dossier: " + numDossier;
    }
}