package ma.ensaj.GestionSurveillance.controllers;

import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.entities.Option;
import ma.ensaj.GestionSurveillance.repositories.OptionRepository;
import ma.ensaj.GestionSurveillance.repositories.SessionRepository;
import ma.ensaj.GestionSurveillance.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    // CREATE: Add a new option
    @PostMapping(value = "/", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public Option addOption(@RequestBody Option option) {
        return optionService.addOption(option);
    }


    // READ: Get all options
    @GetMapping(value = "/", produces = { "application/json", "application/xml" })
    public List<Option> getAllOptions() {
        return optionService.getAllOptions();
    }

    // READ: Get options by department ID
    @GetMapping(value = "/department/{departmentId}", produces = { "application/json", "application/xml" })
    public List<Option> getOptionsByDepartmentId(@PathVariable Long departmentId) {
        return optionService.getOptionsByDepartmentId(departmentId);
    }

    // READ: Get options by name
    @GetMapping(value = "/name/{nom}", produces = { "application/json", "application/xml" })
    public List<Option> getOptionsByNom(@PathVariable String nom) {
        return optionService.getOptionsByNom(nom);
    }

    // READ: Get options by department
    @GetMapping(value = "/department", produces = { "application/json", "application/xml" })
    public List<Option> getOptionsByDepartment(@RequestBody Department department) {
        return optionService.getOptionsByDepartment(department);
    }


    // UPDATE: Update an option
    @PutMapping(value = "/{id}", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<Option> updateOption(@PathVariable Long id, @RequestBody Option optionDetails) {
        Option updatedOption = optionService.updateOption(id, optionDetails);
        return updatedOption != null ? ResponseEntity.ok(updatedOption) : ResponseEntity.notFound().build();
    }

    // DELETE: Delete an option by id
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        boolean isDeleted = optionService.deleteOption(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // GET: Check if an option exists by name and department
    @GetMapping(value = "/exists/{nom}")
    public boolean existsOptionByNomAndDepartment(@PathVariable String nom, @RequestBody Department department) {
        return optionService.existsOptionByNomAndDepartment(nom, department);
    }

    // GET: Count all options
    @GetMapping(value = "/count/all", produces = { "application/json", "application/xml" })
    public long countAllOptions() {
        return optionService.countAllOptions();
    }


    // GET: Count options by department
    @GetMapping(value = "/count/department", produces = { "application/json", "application/xml" })
    public long countOptionsByDepartment(@RequestBody Department department) {
        return optionService.countOptionsByDepartment(department);
    }
    @PostMapping("/import")
    public ResponseEntity<List<Option>> importOptions(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<Option> options = optionService.importOptionsFromCsv(file);
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // OptionController.java

    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Option> getOptionById(@PathVariable Long id) {
        try {
            Option option = optionService.getOptionById(id);
            return ResponseEntity.ok(option);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}

