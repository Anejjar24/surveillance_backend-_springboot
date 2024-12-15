package ma.ensaj.GestionSurveillance.repositories;

import ma.ensaj.GestionSurveillance.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
