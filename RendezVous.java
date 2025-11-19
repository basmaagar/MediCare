import java.io.Serializable;
import java.time.LocalDateTime;

public class RendezVous implements Serializable {
    private static final long serialVersionUID = 1L;
    private Medecin medecin;
    private Patient patient;
    private LocalDateTime dateHeure;

    public RendezVous(Medecin medecin, Patient patient, LocalDateTime dateHeure) {
        this.medecin = medecin;
        this.patient = patient;
        this.dateHeure = dateHeure;

        // Vérification simple (peut être déplacée dans la classe de gestion)
        if (dateHeure.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Le rendez-vous ne peut être dans le passé.");
        }
    }

    // Getters
    public Medecin getMedecin() { return medecin; }
    public Patient getPatient() { return patient; }
    public LocalDateTime getDateHeure() { return dateHeure; }

    @Override
    public String toString() {
        return "RDV le " + dateHeure + " avec " + medecin.getNom() +
                " pour le patient " + patient.getNom();
    }
}