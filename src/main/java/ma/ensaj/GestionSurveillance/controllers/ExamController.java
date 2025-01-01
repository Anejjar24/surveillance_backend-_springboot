package ma.ensaj.GestionSurveillance.controllers;


import ma.ensaj.GestionSurveillance.entities.Exam;
import ma.ensaj.GestionSurveillance.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    @Autowired
    private ExamService examService;

    // Create new exam
    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        Exam savedExam = examService.saveExam(exam);
        return new ResponseEntity<>(savedExam, HttpStatus.CREATED);
    }

    // Get all exams
    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        List<Exam> exams = examService.getAllExams();
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // Get exam by ID
    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        Optional<Exam> exam = examService.getExamById(id);
        return exam.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get exams by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Exam>> getExamsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Exam> exams = examService.getExamsByDate(date);
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // Get exams by department
    @GetMapping("/department/{departementId}")
    public ResponseEntity<List<Exam>> getExamsByDepartement(@PathVariable Long departementId) {
        List<Exam> exams = examService.getExamsByDepartement(departementId);
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // Get exams by module
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<Exam>> getExamsByModule(@PathVariable Long moduleId) {
        List<Exam> exams = examService.getExamsByModule(moduleId);
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // Get exams by option
    @GetMapping("/option/{optionId}")
    public ResponseEntity<List<Exam>> getExamsByOption(@PathVariable Long optionId) {
        List<Exam> exams = examService.getExamsByOption(optionId);
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // Update exam
    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable Long id, @RequestBody Exam examDetails) {
        Exam updatedExam = examService.updateExam(id, examDetails);
        if (updatedExam != null) {
            return new ResponseEntity<>(updatedExam, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete exam
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}