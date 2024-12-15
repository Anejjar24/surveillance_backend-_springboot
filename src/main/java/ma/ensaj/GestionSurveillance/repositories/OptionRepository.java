package ma.ensaj.GestionSurveillance.repositories;

import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    // Find options by department ID
    List<Option> findByDepartment_Id(Long departmentId);

    // Find options by the name (nom)
    List<Option> findByNom(String nom);

    // Find options by department (useful for retrieving all options in a specific department)
    List<Option> findByDepartment(Department department);

    // Count options in a specific department
    long countByDepartment(Department department);

    // Check if an option exists by its name and department
    boolean existsByNomAndDepartment(String nom, Department department);
}
