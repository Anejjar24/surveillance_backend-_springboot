package ma.ensaj.GestionSurveillance.repositories;

import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.entities.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    List<Enseignant> findByDepartment(Department department);

    // Méthode pour compter le nombre d'enseignants dans un département spécifique
    long countByDepartment(Department department);

    // Méthode pour trouver les enseignants qui dispensent
    List<Enseignant> findByDispenseTrue();

    // Méthode pour trouver les enseignants qui ne dispensent pas
    List<Enseignant> findByDispenseFalse();
}
