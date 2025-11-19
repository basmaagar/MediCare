import java.time.LocalDate;

public abstract class Employe extends Personne {
    private String idEmploye;
    private Role role;

    public Employe(String nom, String prenom, LocalDate dateNaissance, String idEmploye, Role role) {
        super(nom, prenom, dateNaissance);
        this.idEmploye = idEmploye;
        this.role = role;
    }

    public String getIdEmploye() { return idEmploye; }
    public Role getRole() { return role; }

    public abstract String afficherDetailsPoste();

    @Override
    public String toString() {
        return super.toString() + ", ID: " + idEmploye + ", Rôle: " + role;
    }
}