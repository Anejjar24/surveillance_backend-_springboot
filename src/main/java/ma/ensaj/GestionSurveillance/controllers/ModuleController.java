package ma.ensaj.GestionSurveillance.controllers;

import ma.ensaj.GestionSurveillance.entities.Module;
import ma.ensaj.GestionSurveillance.entities.Option;
import ma.ensaj.GestionSurveillance.repositories.DepartmentRepository;
import ma.ensaj.GestionSurveillance.repositories.ModuleRepository;
import ma.ensaj.GestionSurveillance.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    // CREATE: Add a new module
    @PostMapping(value = "/", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public Module addModule(@RequestBody Module module) {
        return moduleService.addModule(module);
    }

    // READ: Get all modules
    @GetMapping(value = "/", produces = { "application/json", "application/xml" })
    public List<Module> getAllModules() {
        return moduleService.getAllModules();
    }

    // READ: Get modules by option ID
    @GetMapping(value = "/option/{optionId}", produces = { "application/json", "application/xml" })
    public List<Module> getModulesByOptionId(@PathVariable Long optionId) {
        return moduleService.getModulesByOptionId(optionId);
    }

    // READ: Get modules by option
    @GetMapping(value = "/option", produces = { "application/json", "application/xml" })
    public List<Module> getModulesByOption(@RequestBody Option option) {
        return moduleService.getModulesByOption(option);
    }

    // GET: Count modules by option ID
    @GetMapping(value = "/count/option/{optionId}", produces = { "application/json", "application/xml" })
    public long countModulesByOptionId(@PathVariable Long optionId) {
        return moduleService.countModulesByOptionId(optionId);
    }

    // GET: Count modules by option
    @GetMapping(value = "/count/option", produces = { "application/json", "application/xml" })
    public long countModulesByOption(@RequestBody Option option) {
        return moduleService.countModulesByOption(option);
    }

    // UPDATE: Update a module
    @PutMapping(value = "/{id}", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<Module> updateModule(@PathVariable Long id, @RequestBody Module moduleDetails) {
        Module updatedModule = moduleService.updateModule(id, moduleDetails);
        return updatedModule != null ? ResponseEntity.ok(updatedModule) : ResponseEntity.notFound().build();
    }

    // DELETE: Delete a module by id
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        boolean isDeleted = moduleService.deleteModule(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // GET: Count all modules
    @GetMapping(value = "/count", produces = { "application/json", "application/xml" })
    public long countAllModules() {
        return moduleService.countAllModules();
    }
    @PostMapping("/option/{optionId}/import")
    public ResponseEntity<List<Module>> importModules(
            @PathVariable Long optionId,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<Module> modules = moduleService.importModulesFromCsv(optionId, file);
            return ResponseEntity.ok(modules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
