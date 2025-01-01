package ma.ensaj.GestionSurveillance.services;


import ma.ensaj.GestionSurveillance.entities.Exam;
import ma.ensaj.GestionSurveillance.entities.Locaux;
import ma.ensaj.GestionSurveillance.repositories.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    // Create
    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }

    // Read
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }

    // Get exams by date
    public List<Exam> getExamsByDate(LocalDate date) {
        return examRepository.findByDate(date);
    }

    // Get exams by department
    public List<Exam> getExamsByDepartement(Long departementId) {
        return examRepository.findByDepartement_Id(departementId);
    }

    // Get exams by module
    public List<Exam> getExamsByModule(Long moduleId) {
        return examRepository.findByModule_Id(moduleId);
    }

    // Get exams by option
    public List<Exam> getExamsByOption(Long optionId) {
        return examRepository.findByOption_Id(optionId);
    }

    // Update
    public Exam updateExam(Long id, Exam examDetails) {
        Optional<Exam> exam = examRepository.findById(id);
        if (exam.isPresent()) {
            Exam existingExam = exam.get();
            existingExam.setDate(examDetails.getDate());
            existingExam.setStartTime(examDetails.getStartTime());
            existingExam.setEndTime(examDetails.getEndTime());
            existingExam.setNbrEtudiants(examDetails.getNbrEtudiants());
            existingExam.setSession(examDetails.getSession());
            existingExam.setEnseignant(examDetails.getEnseignant());
            existingExam.setLocaux(examDetails.getLocaux());
            existingExam.setDepartement(examDetails.getDepartement());
            existingExam.setOption(examDetails.getOption());
            existingExam.setModule(examDetails.getModule());
            return examRepository.save(existingExam);
        }
        return null;
    }

    // Delete
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }
}