package ma.ensaj.GestionSurveillance.services;
import com.opencsv.CSVReader;
import ma.ensaj.GestionSurveillance.entities.Module;
import ma.ensaj.GestionSurveillance.entities.Option;
import ma.ensaj.GestionSurveillance.repositories.ModuleRepository;
import ma.ensaj.GestionSurveillance.repositories.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private OptionRepository optionRepository;

    public List<Module> importModulesFromCsv(Long optionId, MultipartFile file) {
        List<Module> modules = new ArrayList<>();

        // Get the Option
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            reader.readNext(); // Skip header line
            while ((nextLine = reader.readNext()) != null) {
                Module module = new Module();
                module.setNom(nextLine[0]);
                module.setOption(option);
                modules.add(module);
            }
            return moduleRepository.saveAll(modules);
        } catch (Exception e) {
            throw new RuntimeException("Error importing modules from CSV: " + e.getMessage());
        }
    }
    // CREATE: Add a new module
    public Module addModule(Module module) {
        return moduleRepository.save(module);
    }

    // READ: Get all modules
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    // READ: Get modules by option ID
    public List<Module> getModulesByOptionId(Long optionId) {
        return moduleRepository.findByOption_Id(optionId);
    }

    // READ: Get modules by option
    public List<Module> getModulesByOption(Option option) {
        return moduleRepository.findByOption(option);
    }

    // READ: Count modules by option
    public long countModulesByOption(Option option) {
        return moduleRepository.countByOption(option);
    }

    // READ: Count modules by option ID
    public long countModulesByOptionId(Long optionId) {
        return moduleRepository.countByOption_Id(optionId);
    }

    // Count all modules
    public long countAllModules() {
        return moduleRepository.count();
    }

    // UPDATE: Update a module by id
    public Module updateModule(Long id, Module moduleDetails) {
        Optional<Module> existingModule = moduleRepository.findById(id);
        if (existingModule.isPresent()) {
            Module module = existingModule.get();
            module.setNom(moduleDetails.getNom());
            module.setOption(moduleDetails.getOption());
            return moduleRepository.save(module);
        }
        return null;
    }

    // DELETE: Delete a module by id
    public boolean deleteModule(Long id) {
        Optional<Module> module = moduleRepository.findById(id);
        if (module.isPresent()) {
            moduleRepository.delete(module.get());
            return true;
        }
        return false;
    }
}
