package ma.ensaj.GestionSurveillance.repositories;
import ma.ensaj.GestionSurveillance.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.Module;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByDate(LocalDate date);
    List<Exam> findByDepartement_Id(Long departementId);
    List<Exam> findByModule_Id(Long moduleId);
    List<Exam> findByOption_Id(Long optionId);
}