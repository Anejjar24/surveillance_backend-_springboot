package ma.ensaj.GestionSurveillance.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "enseignant")
public class Enseignant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String prenom;

    // Relation ManyToOne avec Department

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false) // Clé étrangère
    @JsonBackReference
    private Department department;

    @Column(name = "dispense")
    private boolean dispense;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public boolean isDispense() {
        return dispense;
    }

    public void setDispense(boolean dispense) {
        this.dispense = dispense;
    }
}

