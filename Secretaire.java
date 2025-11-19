import java.time.LocalDate;

public class Secretaire extends Employe {
    private static final long serialVersionUID = 1L;

    public Secretaire(String nom, String prenom, LocalDate dateNaissance, String idEmploye) {
        super(nom, prenom, dateNaissance, idEmploye, Role.SECRETAIRE);
    }

    @Override
    public String afficherDetailsPoste() {
        return "Gestion Administrative et Accueil.";
    }

    @Override
    public String toString() {
        return "Secrétaire " + super.toString();
    }
}