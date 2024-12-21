package ma.ensaj.GestionSurveillance.controllers;

import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.entities.Enseignant;
import ma.ensaj.GestionSurveillance.repositories.DepartmentRepository;
import ma.ensaj.GestionSurveillance.repositories.EnseignantRepository;
import ma.ensaj.GestionSurveillance.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/enseignants")
public class EnseignantController {


    @Autowired
    private EnseignantService enseignantService;

    @PostMapping("/department/{departmentId}/import")
    public ResponseEntity<List<Enseignant>> importEnseignants(
            @PathVariable Long departmentId,
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<Enseignant> enseignants = enseignantService.importEnseignantsFromCsv(departmentId, file);
            return ResponseEntity.ok(enseignants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // CREATE: Add a new enseignant
    @PostMapping(value="/", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public Enseignant addEnseignant(@RequestBody Enseignant enseignant) {
        return enseignantService.addEnseignant(enseignant);
    }

    // READ: Get all enseignants
    @GetMapping(value="/", produces = { "application/json", "application/xml" })
    public List<Enseignant> listEnseignants() {
        return enseignantService.getAllEnseignants();
    }

    // READ: Get an enseignant by id
    @GetMapping(value="/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable Long id) {
        Optional<Enseignant> enseignant = enseignantService.getEnseignantById(id);
        return enseignant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE: Update enseignant by id
    @PutMapping(value="/{id}", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable Long id, @RequestBody Enseignant enseignantDetails) {
        Enseignant updatedEnseignant = enseignantService.updateEnseignant(id, enseignantDetails);
        return updatedEnseignant != null ? ResponseEntity.ok(updatedEnseignant) : ResponseEntity.notFound().build();
    }

    // DELETE: Delete enseignant by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable Long id) {
        boolean isDeleted = enseignantService.deleteEnseignant(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // GET: Get enseignants with dispense status as true
    @GetMapping(value="/dispense", produces = { "application/json", "application/xml" })
    public List<Enseignant> listEnseignantsDispense() {
        return enseignantService.getEnseignantsDispense();
    }

    // GET: Get enseignants with dispense status as false
    @GetMapping(value="/non-dispense", produces = { "application/json", "application/xml" })
    public List<Enseignant> listEnseignantsNonDispense() {
        return enseignantService.getEnseignantsNonDispense();
    }

    // GET: Count enseignants with dispense status as true
    @GetMapping(value="/count-dispense", produces = { "application/json", "application/xml" })
    public long countEnseignantsDispense() {
        return enseignantService.countEnseignantsDispense();
    }

    // GET: Count enseignants with dispense status as false
    @GetMapping(value="/count-non-dispense", produces = { "application/json", "application/xml" })
    public long countEnseignantsNonDispense() {
        return enseignantService.countEnseignantsNonDispense();
    }

    // GET: Get enseignants by department
    @GetMapping(value="/department/{departmentId}", produces = { "application/json", "application/xml" })
    public List<Enseignant> getEnseignantsByDepartment(@PathVariable Long departmentId) {
        Department department = new Department(); // Assume you have a method to fetch department by ID
        department.setId(departmentId);
        return enseignantService.getEnseignantsByDepartment(department);
    }

    // GET: Count enseignants in department
    @GetMapping(value="/department/{departmentId}/count", produces = { "application/json", "application/xml" })
    public long countEnseignantsByDepartment(@PathVariable Long departmentId) {
        Department department = new Department(); // Assume you have a method to fetch department by ID
        department.setId(departmentId);
        return enseignantService.countEnseignantsByDepartment(department);
    }

    // GET: Count all enseignants
    @GetMapping(value="/count", produces = { "application/json", "application/xml" })
    public long countAllEnseignants() {
        return enseignantService.countAllEnseignants();
    }
}

