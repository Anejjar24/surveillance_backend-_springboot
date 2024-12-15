package ma.ensaj.GestionSurveillance.services;

import ma.ensaj.GestionSurveillance.entities.Department;
import ma.ensaj.GestionSurveillance.entities.Option;
import ma.ensaj.GestionSurveillance.repositories.OptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    // CREATE: Add a new option
    public Option addOption(Option option) {
        return optionRepository.save(option);
    }

    // READ: Get all options
    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    // READ: Get options by department ID
    public List<Option> getOptionsByDepartmentId(Long departmentId) {
        return optionRepository.findByDepartment_Id(departmentId);
    }

    // READ: Get options by name
    public List<Option> getOptionsByNom(String nom) {
        return optionRepository.findByNom(nom);
    }

    // READ: Get options by department
    public List<Option> getOptionsByDepartment(Department department) {
        return optionRepository.findByDepartment(department);
    }

    // READ: Count options by department
    public long countOptionsByDepartment(Department department) {
        return optionRepository.countByDepartment(department);
    }

    // UPDATE: Update an option by id
    public Option updateOption(Long id, Option optionDetails) {
        Optional<Option> existingOption = optionRepository.findById(id);
        if (existingOption.isPresent()) {
            Option option = existingOption.get();
            option.setNom(optionDetails.getNom());
            option.setDepartment(optionDetails.getDepartment());
            return optionRepository.save(option);
        }
        return null;
    }

    // DELETE: Delete an option by id
    public boolean deleteOption(Long id) {
        Optional<Option> option = optionRepository.findById(id);
        if (option.isPresent()) {
            optionRepository.delete(option.get());
            return true;
        }
        return false;
    }

    // Check if an option exists by name and department
    public boolean existsOptionByNomAndDepartment(String nom, Department department) {
        return optionRepository.existsByNomAndDepartment(nom, department);
    }

    // READ: Count all options
    public long countAllOptions() {
        return optionRepository.count();
    }
}