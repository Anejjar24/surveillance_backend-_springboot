package ma.ensaj.GestionSurveillance.services;

import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.entities.Enseignant;
import ma.ensaj.GestionSurveillance.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

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
}
