package ma.ensaj.GestionSurveillance.controllers;

import ma.ensaj.GestionSurveillance.entities.Locaux;
import ma.ensaj.GestionSurveillance.entities.Module;
import ma.ensaj.GestionSurveillance.repositories.LocauxRepository;
import ma.ensaj.GestionSurveillance.repositories.ModuleRepository;
import ma.ensaj.GestionSurveillance.services.LocauxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/locals")
public class LocauxController {

    @Autowired
    private LocauxService locauxService;

    @GetMapping("/")
    public List<Locaux> getAllLocaux() {
        return locauxService.getAllLocaux();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locaux> getLocauxById(@PathVariable Long id) {
        Optional<Locaux> locaux = locauxService.getLocalById(id);
        return locaux.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public Locaux createLocaux(@RequestBody Locaux locaux) {
        return locauxService.saveLocal(locaux);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Locaux> updateLocaux(@PathVariable Long id, @RequestBody Locaux locaux) {
        Optional<Locaux> existingLocaux = locauxService.getLocalById(id);
        if (existingLocaux.isPresent()) {
            locaux.setId(id);
            return ResponseEntity.ok(locauxService.saveLocal(locaux));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocaux(@PathVariable Long id) {
        Optional<Locaux> existingLocaux = locauxService.getLocalById(id);
        if (existingLocaux.isPresent()) {
            locauxService.deleteLocal(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/count", produces = { "application/json", "application/xml" })
    public Long countLocaux() {
        return locauxService.countLocaux();
    }

    @PostMapping("/import")
    public ResponseEntity<List<Locaux>> importLocaux(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<Locaux> locaux = locauxService.importLocauxFromCsv(file);
            return ResponseEntity.ok(locaux);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
