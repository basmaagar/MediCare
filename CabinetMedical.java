import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CabinetMedical implements Serializable {

    private final Lock lock = new ReentrantLock();

    private List<Patient> patients = new ArrayList<>();
    private List<Medecin> medecins = new ArrayList<>();
    private List<RendezVous> rendezVous = new ArrayList<>();

    public List<Patient> getPatients() { return patients; }
    public List<Medecin> getMedecins() { return medecins; }
    public List<RendezVous> getRendezVous() { return rendezVous; }


    public void ajouterPatient(Patient p) {
        lock.lock();
        try {
            patients.add(p);
        } finally {
            lock.unlock();
        }
    }

    public void ajouterMedecin(Medecin m) {
        lock.lock();
        try {
            medecins.add(m);
        } finally {
            lock.unlock();
        }
    }

    public void prendreRendezVous(Medecin medecin, Patient patient, LocalDateTime dateHeure) {
        lock.lock();
        try {
            boolean creneauDejaPris = rendezVous.stream()
                    .filter(rdv -> rdv.getMedecin().getIdEmploye().equals(medecin.getIdEmploye()))
                    .anyMatch(rdv -> rdv.getDateHeure().isEqual(dateHeure));

            if (creneauDejaPris) {
                throw new CreneauOccupeException("Le Dr. " + medecin.getNom() + " a déjà un RDV à cette heure.");
            }

            RendezVous nouveauRdv = new RendezVous(medecin, patient, dateHeure);
            rendezVous.add(nouveauRdv);
        } finally {
            lock.unlock();
        }
    }

    public synchronized List<RendezVous> getRendezVousPourMedecin(Medecin medecin) {
        // Le bloc synchronized implicite protège l'accès à 'rendezVous'
        return rendezVous.stream()
                .filter(rdv -> rdv.getMedecin().getIdEmploye().equals(medecin.getIdEmploye()))
                .sorted((r1, r2) -> r1.getDateHeure().compareTo(r2.getDateHeure()))
                .collect(Collectors.toList());
    }


    public void sauvegarderDonnees(String nomFichier) {
        Runnable tacheSauvegarde = () -> {
            lock.lock(); // 1. Le thread de sauvegarde acquiert le verrou
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
                oos.writeObject(this); // Sérialisation complète de l'objet sous le verrou
                System.out.println("\n[THREAD] Sauvegarde asynchrone réussie dans " + nomFichier);
            } catch (IOException e) {
                System.err.println("[THREAD] Erreur de sauvegarde: " + e.getMessage());
            } finally {
                lock.unlock();
            }
        };
        new Thread(tacheSauvegarde).start();
    }


    public static CabinetMedical chargerDonnees(String nomFichier) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomFichier))) {
            return (CabinetMedical) ois.readObject();
        }
    }
}