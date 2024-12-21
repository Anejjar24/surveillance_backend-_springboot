package ma.ensaj.GestionSurveillance.services;

import com.opencsv.CSVReader;
import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.entities.Enseignant;
import ma.ensaj.GestionSurveillance.repositories.DepartmentRepository;
import ma.ensaj.GestionSurveillance.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    // CREATE
    public Enseignant addEnseignant(Enseignant enseignant) {
        return enseignantRepository.save(enseignant);
    }

    // READ: Get all enseignants
    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    // READ: Get enseignants by department
    public List<Enseignant> getEnseignantsByDepartment(Department department) {
        return enseignantRepository.findByDepartment(department);
    }

    // READ: Get enseignants with dispense status as true
    public List<Enseignant> getEnseignantsDispense() {
        return enseignantRepository.findByDispenseTrue();
    }

    // READ: Get enseignants with dispense status as false
    public List<Enseignant> getEnseignantsNonDispense() {
        return enseignantRepository.findByDispenseFalse();
    }

    // READ: Get an enseignant by id
    public Optional<Enseignant> getEnseignantById(Long id) {
        return enseignantRepository.findById(id);
    }

    // UPDATE
    public Enseignant updateEnseignant(Long id, Enseignant enseignantDetails) {
        Optional<Enseignant> existingEnseignant = enseignantRepository.findById(id);
        if (existingEnseignant.isPresent()) {
            Enseignant enseignant = existingEnseignant.get();
            enseignant.setNom(enseignantDetails.getNom());
            enseignant.setPrenom(enseignantDetails.getPrenom());
            enseignant.setDispense(enseignantDetails.isDispense());
            return enseignantRepository.save(enseignant);
        }
        return null;
    }

    // DELETE
    public boolean deleteEnseignant(Long id) {
        Optional<Enseignant> enseignant = enseignantRepository.findById(id);
        if (enseignant.isPresent()) {
            enseignantRepository.delete(enseignant.get());
            return true;
        }
        return false;
    }

    // Count enseignants in department
    public long countEnseignantsByDepartment(Department department) {
        return enseignantRepository.countByDepartment(department);
    }

    // Count enseignants with dispense status
    public long countEnseignantsDispense() {
        return enseignantRepository.findByDispenseTrue().size();
    }

    // Count enseignants without dispense status
    public long countEnseignantsNonDispense() {
        return enseignantRepository.findByDispenseFalse().size();
    }

    // Count all enseignants
    public long countAllEnseignants() {
        return enseignantRepository.count();
    }

    public List<Enseignant> importEnseignantsFromCsv(Long departmentId, MultipartFile file) {
        List<Enseignant> enseignants = new ArrayList<>();

        // Récupérer le département
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            reader.readNext(); // Skip header line
            while ((nextLine = reader.readNext()) != null) {
                Enseignant enseignant = new Enseignant();
                enseignant.setNom(nextLine[0]);
                enseignant.setPrenom(nextLine[1]);
                enseignant.setDispense(Boolean.parseBoolean(nextLine[2]));
                enseignant.setDepartment(department);
                enseignants.add(enseignant);
            }
            return enseignantRepository.saveAll(enseignants);
        } catch (Exception e) {
            throw new RuntimeException("Error importing enseignants from CSV: " + e.getMessage());
        }
    }
}
