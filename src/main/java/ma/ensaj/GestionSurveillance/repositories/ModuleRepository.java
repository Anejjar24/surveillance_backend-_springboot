package ma.ensaj.GestionSurveillance.repositories;

import ma.ensaj.GestionSurveillance.entities.Module;
import ma.ensaj.GestionSurveillance.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    // Count modules by option
    long countByOption(Option option);

    // Count modules by option ID
    long countByOption_Id(Long optionId);
    List<Module> findByOption_Id(Long optionId);

    List<Module> findByOption(Option option);
}
