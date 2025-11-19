import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {

    private static final Path FICHIER_SAUVEGARDE = Paths.get("cabinet_data.txt");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    public static void main(String[] args) {

        CabinetMedical cabinet;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            if (Files.exists(FICHIER_SAUVEGARDE)) {
                try {
                    cabinet = CabinetMedical.chargerDonnees(FICHIER_SAUVEGARDE.toString());
                    System.out.println(" Données du cabinet chargées.");
                } catch (Exception e) {
                    System.err.println(" Erreur de chargement. Nouveau cabinet. Détails: " + e.getMessage());
                    cabinet = new CabinetMedical();
                    initialiserDonnees(cabinet);
                }
            } else {
                cabinet = new CabinetMedical();
                System.out.println(" Fichier de sauvegarde non trouvé. Nouveau cabinet créé.");
                initialiserDonnees(cabinet);
            }

            boolean running = true;
            while (running) {
                afficherMenu();
                String choix = reader.readLine();

                try {
                    switch (choix) {
                        case "1": ajouterPatient(cabinet, reader); break;
                        case "2": prendreRendezVous(cabinet, reader); break;
                        case "3": afficherRDVMedecin(cabinet, reader); break;
                        case "4": cabinet.sauvegarderDonnees(FICHIER_SAUVEGARDE.toString()); break;
                        case "5":
                            running = false;
                            System.out.println("Extinction...");
                            cabinet.sauvegarderDonnees(FICHIER_SAUVEGARDE.toString());
                            Thread.sleep(500);
                            break;
                        default: System.out.println("Option invalide.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Entrée invalide. Veuillez entrer un numéro pour le choix.");
                } catch (Exception e) {
                    System.err.println("Erreur inattendue: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur de lecture/écriture: " + e.getMessage());
        }
    }



    private static void afficherMenu() {
        System.out.println("\n----------------------------------");
        System.out.println("--- MENU CABINET MÉDICAL JAVA ---");
        System.out.println("----------------------------------");
        System.out.println("1. Ajouter un patient");
        System.out.println("2. Prendre un rendez-vous");
        System.out.println("3. Afficher les RDV d'un médecin (Stream)");
        System.out.println("4. Sauvegarder les données (Thread/I/O)");
        System.out.println("5. Quitter");
        System.out.print("Votre choix: ");
    }

    private static void initialiserDonnees(CabinetMedical cabinet) {
        Medecin drDupont = new Medecin("Dupont", "Alain", LocalDate.of(1975, 5, 10), "M001", "Généraliste", "12345");
        cabinet.ajouterMedecin(drDupont);
    }

    private static void ajouterPatient(CabinetMedical cabinet, BufferedReader reader) throws IOException {
        System.out.println("\n-- AJOUT PATIENT --");
        System.out.print("Nom: "); String nom = reader.readLine();
        System.out.print("Prénom: "); String prenom = reader.readLine();
        System.out.print("Date de naissance (JJ/MM/AAAA): "); String dateStr = reader.readLine();
        System.out.print("Numéro de dossier: "); String numDossier = reader.readLine();

        try {
            LocalDate dateNaissance = LocalDate.parse(dateStr, DATE_FORMAT);
            Patient p = new Patient(nom, prenom, dateNaissance, numDossier);
            cabinet.ajouterPatient(p);
            System.out.println(" Patient " + nom + " ajouté avec succès.");
        } catch (DateTimeParseException e) {
            System.err.println(" Format de date incorrect. Utilisez JJ/MM/AAAA.");
        } catch (DateDeNaissanceFutureException e) {
            System.err.println(" ERREUR: " + e.getMessage());
        }
    }

    private static void prendreRendezVous(CabinetMedical cabinet, BufferedReader reader) throws IOException {
        if (cabinet.getMedecins().isEmpty()) { System.err.println("Veuillez d'abord ajouter un médecin."); return; }

        System.out.println("\n-- PRISE DE RDV --");
        List<Medecin> medecins = cabinet.getMedecins();
        for (int i = 0; i < medecins.size(); i++) { System.out.println(i + ". " + medecins.get(i).getNom()); }
        System.out.print("Choisissez le numéro du médecin: ");
        int medIndex = Integer.parseInt(reader.readLine());
        Medecin medecin = medecins.get(medIndex);

        System.out.print("Entrez le numéro de dossier du patient: ");
        String numDossier = reader.readLine();

        Patient patient = cabinet.getPatients().stream()
                .filter(p -> p.getNumDossier().equals(numDossier))
                .findFirst()
                .orElse(null);

        if (patient == null) { System.err.println(" Patient non trouvé."); return; }

        System.out.print("Date et heure du RDV (JJ/MM/AAAA HH:mm): ");
        String dtStr = reader.readLine();

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dtStr, DATETIME_FORMAT);
            cabinet.prendreRendezVous(medecin, patient, dateTime);
            System.out.println(" Rendez-vous enregistré.");
        } catch (DateTimeParseException e) {
            System.err.println(" Format de date/heure incorrect. Utilisez JJ/MM/AAAA HH:mm.");
        } catch (CreneauOccupeException e) {
            System.err.println(" ERREUR: " + e.getMessage());
        }
    }

    private static void afficherRDVMedecin(CabinetMedical cabinet, BufferedReader reader) throws IOException {
        if (cabinet.getMedecins().isEmpty()) { System.err.println("Aucun médecin n'est enregistré."); return; }

        System.out.println("\n-- AFFICHAGE RDV --");
        List<Medecin> medecins = cabinet.getMedecins();
        for (int i = 0; i < medecins.size(); i++) { System.out.println(i + ". " + medecins.get(i).getNom()); }
        System.out.print("Choisissez le numéro du médecin: ");
        int medIndex = Integer.parseInt(reader.readLine());
        Medecin medecin = medecins.get(medIndex);

        List<RendezVous> rdvList = cabinet.getRendezVousPourMedecin(medecin);

        if (rdvList.isEmpty()) {
            System.out.println("Aucun rendez-vous trouvé pour le Dr. " + medecin.getNom() + ".");
        } else {
            System.out.println("\nListe des rendez-vous du Dr. " + medecin.getNom() + " (filtrés et triés par Stream):");
            for (RendezVous rdv : rdvList) {
                System.out.println("- " + rdv.getDateHeure().format(DATETIME_FORMAT) +
                        " - Patient: " + rdv.getPatient().getPrenom() + " " + rdv.getPatient().getNom());
            }
        }
    }
}













