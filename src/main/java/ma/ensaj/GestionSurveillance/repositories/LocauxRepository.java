package ma.ensaj.GestionSurveillance.repositories;

import ma.ensaj.GestionSurveillance.entities.Enseignant;
import ma.ensaj.GestionSurveillance.entities.Locaux;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocauxRepository extends JpaRepository<Locaux, Long> {
}
